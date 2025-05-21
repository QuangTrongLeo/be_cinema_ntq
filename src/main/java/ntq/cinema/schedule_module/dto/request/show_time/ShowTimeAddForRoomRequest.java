package ntq.cinema.schedule_module.dto.request.show_time;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
public class ShowTimeAddForRoomRequest {
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;
}
