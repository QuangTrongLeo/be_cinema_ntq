package ntq.cinema.schedule_module.dto.request.room;

import lombok.Data;

@Data
public class RoomDeleteFromScheduleRequest {
    private long roomId;
    private long scheduleId;
}
