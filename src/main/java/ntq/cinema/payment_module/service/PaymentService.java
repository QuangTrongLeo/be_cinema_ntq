package ntq.cinema.payment_module.service;

import lombok.AllArgsConstructor;
import ntq.cinema.auth_module.service.EmailService;
import ntq.cinema.booking_module.entity.Booking;
import ntq.cinema.booking_module.entity.BookingSeat;
import ntq.cinema.booking_module.service.BookingService;
import ntq.cinema.payment_module.config.VnPayConfig;
import ntq.cinema.payment_module.entity.Payment;
import ntq.cinema.payment_module.entity.PaymentStatus;
import ntq.cinema.payment_module.enums.PaymentStatusEnum;
import ntq.cinema.payment_module.repository.PaymentRepository;
import ntq.cinema.payment_module.repository.PaymentStatusRepository;
import ntq.cinema.ticket_module.entity.Ticket;
import ntq.cinema.ticket_module.repository.TicketRepository;
import ntq.cinema.ticket_module.service.QrCodeService;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentService {
    private final VnPayConfig vnPayConfig;
    private final PaymentRepository paymentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final TicketRepository ticketRepository;
    private final BookingService bookingService;
    private final EmailService emailService;
    private final QrCodeService qrCodeService;

    public String createVnPayUrl(long bookingId, int amount, String ipAddress) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderInfo = "Thanh toán đơn vé #" + bookingId;
            String orderType = "billpayment";

            String txnRef = String.valueOf(bookingId); // dùng bookingId làm mã đơn hàng

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String createDate = LocalDateTime.now().format(formatter);

            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", vnp_Version);
            vnpParams.put("vnp_Command", vnp_Command);
            vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());
            vnpParams.put("vnp_Amount", String.valueOf(amount * 100)); // VNPAY quy định x100
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", txnRef);
            vnpParams.put("vnp_OrderInfo", orderInfo);
            vnpParams.put("vnp_OrderType", orderType);
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
            vnpParams.put("vnp_IpAddr", ipAddress);
            vnpParams.put("vnp_CreateDate", createDate);

            // Sắp xếp theo thứ tự alphabet
            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (String fieldName : fieldNames) {
                String value = vnpParams.get(fieldName);
                if (hashData.length() > 0) {
                    hashData.append('&');
                    query.append('&');
                }
                hashData.append(fieldName).append('=').append(value);
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append('=')
                        .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            }

            String secureHash = hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
            query.append("&vnp_SecureHash=").append(secureHash);

            return vnPayConfig.getVnpayUrl() + "?" + query.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo URL thanh toán VNPAY", e);
        }
    }

    // XỬ LÝ NÊÚ NHƯ THANH TOÁN THÀNH CÔNG
    public void handlePaymentSuccess(long bookingId){
        Booking booking = bookingService.findBookingById(bookingId);
        Payment payment = findPaymentByBooking(booking);

        payment.setStatus(findByStatusOfPayment(PaymentStatusEnum.SUCCESS));
        payment.setPaymentTime(Timestamp.valueOf(LocalDateTime.now()));
        paymentRepository.save(payment);

        // Kiểm tra nếu ticket đã tồn tại thì không tạo ra
        if (booking.getTicket() == null) {
            // Tạo mã QR
            String qrCodeUrl = qrCodeService.generateQrCode(booking);

            Ticket ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setQrCodeUrl(qrCodeUrl);
            ticket.setSentToEmail(false);
            ticket.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));

            // Lưu Ticket
            ticketRepository.save(ticket);

            // Gửi email có chứa mã QR
            emailService.sendTicketEmail(booking.getUser(), booking.getBookingSeats(), qrCodeUrl);
            ticket.setSentToEmail(true);
            ticketRepository.save(ticket);
        }
    }

    // XỬ LÝ NẾU NHƯ THANH TOÁN THẤT BẠI
    public void handlePaymentFail(long bookingId){
        bookingService.deleteBookingById(bookingId);
    }

    // TÌM TRẠNG THÁI CỦA PAYMENT
    public PaymentStatus findByStatusOfPayment(PaymentStatusEnum status){
        return paymentStatusRepository.findByStatus(status)
                .orElseThrow(() -> new RuntimeException("Payment status " + status.name() + " not found"));
    }

    // Tìm Payment bằng Booking
    private Payment findPaymentByBooking(Booking booking){
        Payment payment = booking.getPayment();
        if (payment == null) {
            throw new RuntimeException("Không tìm thấy Payment!");
        }
        return payment;
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac.init(secretKey);
        byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}
