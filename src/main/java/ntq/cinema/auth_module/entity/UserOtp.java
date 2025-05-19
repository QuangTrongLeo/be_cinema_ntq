package ntq.cinema.auth_module.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

// UserOtp.java
@Entity
@Table(name = "user_otp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "otp_code", nullable = false)
    private String otpCode;

    @Column(name = "expiry_time", nullable = false)
    private Timestamp expiryTime;

    @Column(name = "is_used")
    private boolean isUsed = false;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    public UserOtp(User user, String otpCode, Timestamp expiryTime, Timestamp createdAt) {
        this.user = user;
        this.otpCode = otpCode;
        this.expiryTime = expiryTime;
        this.createdAt = createdAt;
        this.isUsed = false;
    }
}

