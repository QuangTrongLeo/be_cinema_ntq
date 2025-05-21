package ntq.cinema.schedule_module.dto.response.schedule;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
public class ScheduleResponse {
    private long scheduleId;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
}
