package ntq.cinema.payment_module.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntq.cinema.payment_module.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${api.ntq-cinema-url}/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/vnpay")
    public ResponseEntity<?> createVnPayUrl(@RequestParam("bookingId") long bookingId,
                                            @RequestParam("amount") int amount,
                                            HttpServletRequest request){
        try {
            String ip = request.getRemoteAddr();
            String paymentString = paymentService.createVnPayUrl(bookingId, amount, ip);
            return ResponseEntity.ok(paymentString);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> params) {
        try {
            String responseCode = params.get("vnp_ResponseCode");
            String bookingIdStr = params.get("vnp_TxnRef");
            Long bookingId = Long.parseLong(bookingIdStr);

            if ("00".equals(responseCode)) {
                paymentService.handlePaymentSuccess(bookingId);
                return ResponseEntity.ok("Thanh toán thành công!");
            } else {
                paymentService.handlePaymentFail(bookingId);
                return ResponseEntity.ok("Thanh toán thất bại!");
            }
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }
}
