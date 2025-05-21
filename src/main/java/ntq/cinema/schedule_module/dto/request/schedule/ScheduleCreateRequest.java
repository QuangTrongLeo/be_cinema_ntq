package ntq.cinema.schedule_module.dto.request.schedule;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ScheduleCreateRequest {
    private long movieId;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
}
