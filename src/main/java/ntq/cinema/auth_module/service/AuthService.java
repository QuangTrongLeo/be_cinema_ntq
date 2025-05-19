package ntq.cinema.auth_module.service;

import ntq.cinema.auth_module.dto.request.LoginRequest;
import ntq.cinema.auth_module.dto.request.RegisterRequest;
import ntq.cinema.auth_module.dto.request.VerifyOtpRequest;
import ntq.cinema.auth_module.dto.response.JwtResponse;
import ntq.cinema.auth_module.entity.*;
import ntq.cinema.auth_module.enums.RoleTypeEnum;
import ntq.cinema.auth_module.mapper.JwtMapper;
import ntq.cinema.auth_module.repository.RefreshTokenRepository;
import ntq.cinema.auth_module.repository.RoleRepository;
import ntq.cinema.auth_module.repository.UserOtpRepository;
import ntq.cinema.auth_module.repository.UserRepository;
import ntq.cinema.auth_module.util.RandomOtpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
//@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserOtpRepository otpRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final JwtMapper jwtMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenExpiration;
    @Value("${otp.expiry.minutes}")
    private long otpExpiryMinutes;

    public AuthService(UserRepository userRepository,
                       UserOtpRepository otpRepository,
                       RoleRepository roleRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       EmailService emailService,
                       JwtService jwtService, JwtMapper jwtMapper) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.jwtMapper = jwtMapper;
    }

    // ===== 1. TẠO TÀI KHOẢN =====
    // 1.1. ĐĂNG KÝ TÀI KHOẢN BẰNG EMAIL
    public void register(RegisterRequest request) {
        // 1.1.1. Kiểm tra email đăng ký đã tồn tại hay chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Tài khoản email này đã tồn tại !");
        }


        // Tìm role CUSTOMER
        Role defaultCustomerRole = roleRepository.findByName(RoleTypeEnum.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Vai trò CUSTOMER không tồn tại trong hệ thống"));

        // Tạo user mới
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setEnabled(false);

        // Tạo UserRole
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(defaultCustomerRole);

        // Gắn vào user
        user.getUserRoles().add(userRole);

        // Lưu user (sẽ cascade lưu luôn UserRole)
        userRepository.save(user);

        // Tạo và lưu OTP
        String otp = RandomOtpUtil.generateOtp();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiry = Timestamp.from(Instant.now().plus(otpExpiryMinutes, ChronoUnit.MINUTES));
        UserOtp userOtp = new UserOtp(user, otp, expiry, now);
        otpRepository.save(userOtp);

        // Gửi OTP qua email
        emailService.sendOtpEmail(user.getEmail(), otp);
    }


    // 1.2. XÁC THỰC OTP
    public void verifyOtp(VerifyOtpRequest request) {
        UserOtp otp = otpRepository.findByUserEmailAndOtpCode(request.getEmail(), request.getOtpCode());
        if (otp == null || otp.isUsed() || otp.getExpiryTime().before(new Timestamp(System.currentTimeMillis()))) {
            throw new RuntimeException("OTP không hợp lệ hoặc đã hết hạn");
        }

        otp.setUsed(true);
        otpRepository.save(otp);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        user.setEnabled(true);
        userRepository.save(user);
    }

    // ===== 2. ĐĂNG NHẬP =====
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Tài khoản chưa được kích hoạt");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Lưu refreshToken vào DB
        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setUser(user);
        tokenEntity.setToken(refreshToken);
        tokenEntity.setExpiryDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)));
        refreshTokenRepository.save(tokenEntity);

        return jwtMapper.toJwtResponse(accessToken, refreshToken);
    }

    public JwtResponse refreshAccessToken(String refreshToken) {
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token không hợp lệ"));

        if (tokenEntity.getExpiryDate().before(new Timestamp(System.currentTimeMillis()))) {
            refreshTokenRepository.delete(tokenEntity);
            throw new RuntimeException("Refresh token đã hết hạn");
        }

        User user = tokenEntity.getUser();

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Cập nhật token mới vào DB (tuỳ chọn: xóa token cũ, hoặc cập nhật lại)
        tokenEntity.setToken(newRefreshToken);
        tokenEntity.setExpiryDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)));
        refreshTokenRepository.save(tokenEntity);

        return jwtMapper.toJwtResponse(newAccessToken, newRefreshToken);
    }

    // ===== #. ĐĂNG XUẤT =====
    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token không hợp lệ"));
        refreshTokenRepository.delete(token); // hoặc đánh dấu là revoked
    }

}
