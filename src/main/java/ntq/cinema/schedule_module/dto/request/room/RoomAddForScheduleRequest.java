package ntq.cinema.schedule_module.dto.request.room;

import lombok.Data;

@Data
public class RoomAddForScheduleRequest {
    private long roomId;
    private long scheduleId;
}
