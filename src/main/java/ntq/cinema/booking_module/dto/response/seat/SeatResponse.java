package ntq.cinema.booking_module.dto.response.seat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatResponse {
    private long seatId;
    private String seatName;
    private int seatPrice;
    private String statusName;
    private String roomName;
}
