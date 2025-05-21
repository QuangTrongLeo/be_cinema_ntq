package ntq.cinema.schedule_module.dto.response.show_time;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
public class ShowTimeResponse {
    private long showtimeId;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;
}
