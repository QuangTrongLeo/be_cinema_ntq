package ntq.cinema.schedule_module.dto.response.room;

import lombok.Builder;
import lombok.Data;
import ntq.cinema.booking_module.dto.response.seat.SeatResponse;

import java.util.List;

@Data
@Builder
public class RoomWithSeatsResponse {
    private long roomId;
    private String roomName;
    private List<SeatResponse> seats;
}
