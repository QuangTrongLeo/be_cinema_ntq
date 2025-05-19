package ntq.cinema.booking_module.dto.request.seat;

import lombok.Data;
import ntq.cinema.booking_module.entity.SeatStatus;

@Data
public class SeatCreateRequest {
    private long roomId;
    private String roomName;
    private int roomPrice;
    private SeatStatus status;
}
