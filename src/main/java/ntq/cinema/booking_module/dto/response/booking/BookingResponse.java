package ntq.cinema.booking_module.dto.response.booking;

import lombok.Builder;
import lombok.Data;
import ntq.cinema.booking_module.dto.response.seat.SeatResponse;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    private Long bookingId;
    private Long userId;
    private Long showTimeId;
    private Timestamp createdAt;
    private List<SeatResponse> bookedSeats;
    private int totalAmount;
}
