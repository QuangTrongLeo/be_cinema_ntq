package ntq.cinema.schedule_module.service;

import jakarta.transaction.Transactional;
import ntq.cinema.booking_module.dto.response.seat.SeatResponse;
import ntq.cinema.booking_module.entity.Seat;
import ntq.cinema.booking_module.mapper.SeatMapper;
import ntq.cinema.booking_module.repository.SeatRepository;
import ntq.cinema.booking_module.service.SeatService;
import ntq.cinema.schedule_module.dto.request.room.RoomCreateRequest;
import ntq.cinema.schedule_module.dto.request.room.RoomUpdateRequest;
import ntq.cinema.schedule_module.dto.response.room.RoomResponse;
import ntq.cinema.schedule_module.dto.response.room.RoomWithSeatsResponse;
import ntq.cinema.schedule_module.entity.Room;
import ntq.cinema.schedule_module.mapper.RoomMapper;
import ntq.cinema.schedule_module.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final SeatService seatService;
    private final SeatRepository seatRepository;
    private final RoomMapper roomMapper;
    private final SeatMapper seatMapper;

    public RoomService(RoomRepository roomRepository,
                       SeatService seatService,
                       SeatRepository seatRepository,
                       RoomMapper roomMapper,
                       SeatMapper seatMapper) {
        this.roomRepository = roomRepository;
        this.seatService = seatService;
        this.seatRepository = seatRepository;
        this.roomMapper = roomMapper;
        this.seatMapper = seatMapper;
    }

    // DANH SÁCH PHÒNG CHIẾU
    public List<RoomResponse> getAllRooms(){
        List<Room> rooms = roomRepository.findAll();
        return roomMapper.mapperToResponseList(rooms);
    }

    // CHỌN PHÒNG CHIẾU
    public RoomWithSeatsResponse getRoomByRoomId(long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với ID: " + roomId));

        List<Seat> seats = seatRepository.findByRoom(room);
        List<SeatResponse> seatResponses = seatMapper.mapperToResponseList(seats);

        return roomMapper.mapperToSeatsResponse(room, seatResponses);
    }

    // TẠO PHÒNG CHIẾU
    @Transactional
    public RoomResponse createRoom(RoomCreateRequest request) {
        boolean existingRoom = roomRepository.existsByName(request.getRoomName());
        if (existingRoom) {
            throw new RuntimeException("Tên phòng đã tồn tại");
        }
        Room room = new Room();
        room.setName(request.getRoomName());
        room = roomRepository.save(room);

        // Tạo 160 ghế từ A01 đến J16 mặc định khi tạo phòng
        seatService.createSeatsForRoom(room);

        return roomMapper.mapperToResponse(room);
    }

    // CẬP NHẬT (TÊN) PHÒNG CHIẾU
    @Transactional
    public RoomResponse updateRoom(RoomUpdateRequest request) {
        Optional<Room> room = roomRepository.findById(request.getRoomId());
        if (room.isEmpty()) {
            throw new RuntimeException("Không tìm thấy phòng!");
        }

        Room roomUpdate = room.get();
        roomUpdate.setName(request.getRoomName());
        return roomMapper.mapperToResponse(roomRepository.save(roomUpdate));
    }

    // XÓA PHÒNG CHIẾU
    @Transactional
    public void deleteRoom(long roomId){
        if (!roomRepository.existsById(roomId)) {
            throw new RuntimeException("Không tìm thấy phòng!");
        }
        seatRepository.deleteByRoom_RoomId(roomId);
        roomRepository.deleteById(roomId);
    }
}
