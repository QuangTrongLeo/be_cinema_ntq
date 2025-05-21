package ntq.cinema.auth_module.repository;

import ntq.cinema.auth_module.entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {
    UserOtp findByUserEmailAndOtpCode(String email, String otpCode);

    Optional<UserOtp> findTopByUserEmailOrderByExpiryTimeDesc(String email);
}
