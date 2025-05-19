package ntq.cinema.auth_module.repository;

import ntq.cinema.auth_module.entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {
    UserOtp findByUserEmailAndOtpCode(String email, String otpCode);
}
