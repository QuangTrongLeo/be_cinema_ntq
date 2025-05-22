package ntq.cinema.booking_module.dto.request.booking;

import lombok.Data;

import java.util.List;

@Data
public class BookingCreateRequest {
    private Long userId;
    private Long showTimeId;
    private List<Long> seatIds;
}
