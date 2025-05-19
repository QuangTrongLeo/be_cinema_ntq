package ntq.cinema.schedule_module.dto.response.room;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomResponse {
    private long roomId;
    private String roomName;
}
