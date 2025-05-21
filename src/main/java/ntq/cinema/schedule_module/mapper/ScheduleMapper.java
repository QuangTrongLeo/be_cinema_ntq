package ntq.cinema.schedule_module.mapper;

import ntq.cinema.schedule_module.dto.response.schedule.ScheduleResponse;
import ntq.cinema.schedule_module.entity.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduleMapper {
    public ScheduleResponse mapperToResponse(Schedule schedule) {
        if (schedule == null) { return null; }

        return ScheduleResponse.builder()
                .scheduleId(schedule.getScheduleId())
                .date(schedule.getDate())
                .build();
    }

    public List<ScheduleResponse> mapperToResponseList(List<Schedule> rooms) {
        return rooms.stream()
                .map(this::mapperToResponse)
                .collect(Collectors.toList());
    }
}
