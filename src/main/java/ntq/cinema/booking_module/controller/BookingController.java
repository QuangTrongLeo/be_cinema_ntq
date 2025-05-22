package ntq.cinema.booking_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.booking_module.dto.request.booking.BookingCreateRequest;
import ntq.cinema.booking_module.dto.response.booking.BookingResponse;
import ntq.cinema.booking_module.service.BookingService;
import ntq.cinema.movie_module.dto.response.movie.MovieResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.ntq-cinema-url}/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/cus")
    public ResponseEntity<?> createBooking(@RequestBody BookingCreateRequest request) {
        try {
            BookingResponse response = bookingService.createBookingForCus(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

}
