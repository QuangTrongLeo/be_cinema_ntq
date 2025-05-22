package ntq.cinema.auth_module.service;

import jakarta.mail.internet.MimeMessage;
import ntq.cinema.auth_module.entity.User;
import ntq.cinema.booking_module.entity.Booking;
import ntq.cinema.booking_module.entity.BookingSeat;
import ntq.cinema.schedule_module.entity.ShowTime;
import ntq.cinema.ticket_module.entity.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // GỬI OTP ĐẾN EMAIL
    public void sendOtpEmail(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Xác nhận đăng ký tài khoản - NTQ Cinema");
        message.setText("Cảm ơn bạn đã sử dụng dịch vụ của NTQ Cinema.\n\nMã OTP của bạn là: " + otpCode + "\n\nOTP sẽ hết hạn sau 5 phút.");
        mailSender.send(message);
    }

    // GỬI TICKET ĐẾN EMAIL
    public void sendTicketEmail(User user, List<BookingSeat> bookingSeats, String qrCodeUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(user.getEmail());
            helper.setSubject("🎟️ Vé xem phim của bạn - NTQ Cinema");

            StringBuilder content = new StringBuilder();
            content.append("<p>Chào <strong>").append(user.getUsername()).append("</strong>,</p>")
                    .append("<p>Cảm ơn bạn đã đặt vé thành công tại <strong>NTQ Cinema</strong>!</p>")
                    .append("<p>Dưới đây là thông tin vé của bạn:</p>");

            if (!bookingSeats.isEmpty()) {
                Booking booking = bookingSeats.get(0).getBooking();
                ShowTime showTime = booking.getShowTime();
                String time = showTime.getTime().toString();
                String room = "Phòng " + showTime.getRoom().getName();
                String seats = bookingSeats.stream()
                        .map(bs -> bs.getSeat().getName())
                        .collect(Collectors.joining(", "));

                content.append("<ul>")
                        .append("<li><strong>📅 Suất chiếu:</strong> ").append(time).append("</li>")
                        .append("<li><strong>🏠 Phòng chiếu:</strong> ").append(room).append("</li>")
                        .append("<li><strong>💺 Ghế:</strong> ").append(seats).append("</li>")
                        .append("<li><strong>🧾 Mã đặt chỗ:</strong> ").append(booking.getBookingId()).append("</li>")
                        .append("</ul>");
            }

            content.append("<p><strong>👉 Mã QR của bạn:</strong></p>")
                    .append("<img src='").append(qrCodeUrl).append("' alt='QR Code' style='width:200px;height:200px;'/>")
                    .append("<p>Vui lòng trình mã QR này tại rạp để được vào phòng chiếu.</p>")
                    .append("<p>🎉 Chúc bạn xem phim vui vẻ!</p>");

            helper.setText(content.toString(), true); // true để gửi HTML

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Gửi email thất bại", e);
        }
    }
}

