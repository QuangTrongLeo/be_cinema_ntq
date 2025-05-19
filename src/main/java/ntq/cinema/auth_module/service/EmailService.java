package ntq.cinema.auth_module.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Xác nhận đăng ký tài khoản - NTQ Cinema");
        message.setText("Cảm ơn bạn đã sử dụng dịch vụ của NTQ Cinema.\n\nMã OTP của bạn là: " + otpCode + "\n\nOTP sẽ hết hạn sau 5 phút.");
        mailSender.send(message);
    }
}

