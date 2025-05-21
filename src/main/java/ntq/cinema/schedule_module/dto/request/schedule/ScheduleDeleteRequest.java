package ntq.cinema.schedule_module.dto.request.schedule;

import lombok.Data;

@Data
public class ScheduleDeleteRequest {
    private long movieId;
    private long scheduleId;
}
