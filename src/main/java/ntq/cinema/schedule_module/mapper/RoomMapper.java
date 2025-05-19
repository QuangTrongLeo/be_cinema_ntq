package ntq.cinema.schedule_module.mapper;

import ntq.cinema.booking_module.dto.response.seat.SeatResponse;
import ntq.cinema.schedule_module.dto.response.room.RoomResponse;
import ntq.cinema.schedule_module.dto.response.room.RoomWithSeatsResponse;
import ntq.cinema.schedule_module.entity.Room;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {

    public RoomResponse mapperToResponse(Room room) {
        if (room == null) { return null; }

        return RoomResponse.builder()
                .roomId(room.getRoomId())
                .roomName(room.getName())
                .build();
    }

    public RoomWithSeatsResponse mapperToSeatsResponse(Room room, List<SeatResponse> seatResponses){
        if (room == null) { return null; }

        return RoomWithSeatsResponse.builder()
                .roomId(room.getRoomId())
                .roomName(room.getName())
                .seats(seatResponses)
                .build();
    }

    public List<RoomResponse> mapperToResponseList(List<Room> rooms) {
        return rooms.stream()
                .map(this::mapperToResponse)
                .collect(Collectors.toList());
    }
}
