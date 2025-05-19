package ntq.cinema.booking_module.dto.request.seat;

import lombok.Data;

@Data
public class SeatUpdateStatusRequest {
    private long seatId;
    private long seatStatusId;
}
