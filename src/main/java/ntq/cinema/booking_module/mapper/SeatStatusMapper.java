package ntq.cinema.booking_module.mapper;

import ntq.cinema.booking_module.dto.response.seat.SeatStatusResponse;
import ntq.cinema.booking_module.entity.SeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeatStatusMapper {
    public SeatStatusResponse mapperToResponse(SeatStatus seatStatus) {
        return SeatStatusResponse.builder()
                .statusId(seatStatus.getStatusId())
                .statusName(seatStatus.getName())
                .build();
    }

    public List<SeatStatusResponse> mapperToResponseList(List<SeatStatus> seatStatuses) {
        return seatStatuses.stream()
                .map(this::mapperToResponse)
                .collect(Collectors.toList());
    }
}
