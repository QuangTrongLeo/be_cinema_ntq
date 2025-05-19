package ntq.cinema.booking_module.dto.request.seat;

import lombok.Data;

@Data
public class SeatUpdateNameRequest {
    private long seatId;
    private String seatName;
}
