package ntq.cinema.ticket_module.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.AllArgsConstructor;
import ntq.cinema.booking_module.entity.Booking;
import ntq.cinema.schedule_module.entity.ShowTime;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QrCodeService {

    // TẠO MÁ QR
    public String generateQrCode(Booking booking) {
        ShowTime showTime = booking.getShowTime();
        String time = showTime.getTime().toString();
        String room = "Room " + showTime.getRoom().getName();

        String content = "Booking ID: " + booking.getBookingId() + "\n"
                + "Showtime: " + time + "\n"
                + "Room: " + room + "\n"
                + "Seats: " + booking.getBookingSeats().stream()
                .map(bs -> bs.getSeat().getName()) // dùng seatName nếu muốn in A1, A2,...
                .collect(Collectors.joining(", "));
        return generateQrCodeImageUrl(content);
    }

    // TẠO ẢNH QR
    private String generateQrCodeImageUrl(String content) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 250, 250);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo mã QR", e);
        }
    }
}
