package ntq.cinema.booking_module.mapper;

import ntq.cinema.booking_module.dto.response.seat.SeatResponse;
import ntq.cinema.booking_module.entity.Seat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeatMapper {
    public SeatResponse mapperToResponse(Seat seat) {
        return SeatResponse.builder()
                .seatId(seat.getSeatId())
                .seatName(seat.getName())
                .seatPrice(seat.getPrice())
                .statusName(seat.getStatus().getName().name())
                .roomName(seat.getRoom() != null ? seat.getRoom().getName() : null)
                .build();
    }

    public List<SeatResponse> mapperToResponseList(List<Seat> seats) {
        return seats.stream()
                .map(this::mapperToResponse)
                .collect(Collectors.toList());
    }
}
