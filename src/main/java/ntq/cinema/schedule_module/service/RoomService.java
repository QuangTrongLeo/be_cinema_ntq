package ntq.cinema.schedule_module.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ntq.cinema.booking_module.dto.response.seat.SeatResponse;
import ntq.cinema.booking_module.entity.Seat;
import ntq.cinema.booking_module.mapper.SeatMapper;
import ntq.cinema.booking_module.repository.SeatRepository;
import ntq.cinema.booking_module.service.SeatService;
import ntq.cinema.schedule_module.dto.request.room.RoomAddForScheduleRequest;
import ntq.cinema.schedule_module.dto.request.room.RoomCreateRequest;
import ntq.cinema.schedule_module.dto.request.room.RoomDeleteFromScheduleRequest;
import ntq.cinema.schedule_module.dto.request.room.RoomUpdateRequest;
import ntq.cinema.schedule_module.dto.response.room.RoomResponse;
import ntq.cinema.schedule_module.dto.response.room.RoomWithSeatsResponse;
import ntq.cinema.schedule_module.entity.Room;
import ntq.cinema.schedule_module.entity.Schedule;
import ntq.cinema.schedule_module.mapper.RoomMapper;
import ntq.cinema.schedule_module.repository.RoomRepository;
import ntq.cinema.schedule_module.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final SeatService seatService;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final RoomMapper roomMapper;
    private final SeatMapper seatMapper;
    private final ScheduleService scheduleService;


    // DANH SÁCH PHÒNG CHIẾU
    public List<RoomResponse> getAllRooms(){
        List<Room> rooms = roomRepository.findAll();
        return roomMapper.mapperToResponseList(rooms);
    }

    // CHỌN PHÒNG CHIẾU
    public RoomWithSeatsResponse getRoomByRoomId(long roomId) {
        Room room = getRoomById(roomId);

        List<Seat> seats = seatRepository.findByRoom(room);
        List<SeatResponse> seatResponses = seatMapper.mapperToResponseList(seats);

        return roomMapper.mapperToSeatsResponse(room, seatResponses);
    }

    // <========== PHÒNH CHIẾU ===========>
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
        Room roomUpdate = getRoomById(request.getRoomId());
        roomUpdate.setName(request.getRoomName());
        roomRepository.save(roomUpdate);
        return roomMapper.mapperToResponse(roomUpdate);
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

    // <========== PHÒNG CHIẾU VÀ LỊCH CHIẾU ==========>
    // THÊM PHÒNG CHIẾU VÀO LỊCH CHIẾU
    @Transactional
    public RoomResponse addRoomForSchedule(RoomAddForScheduleRequest request){
        Room room = getRoomById(request.getRoomId());
        Schedule schedule = scheduleService.getScheduleById(request.getScheduleId());

        // Kiểm tra phòng đã có trong lích chiếu hay chưa
        if (!schedule.getRooms().contains(room)) {
            schedule.getRooms().add(room);
            scheduleRepository.save(schedule);
        }

        return roomMapper.mapperToResponse(room);
    }

    // XÓA PHÒNG CHIẾU RA KHỎI LỊCH CHIẾU

    @Transactional
    public void deleteRoomFromSchedule(RoomDeleteFromScheduleRequest request){
        Room room = getRoomById(request.getRoomId());
        Schedule schedule = scheduleService.getScheduleById(request.getScheduleId());
        // Kiểm tra phòng đã có trong lích chiếu hay chưa
        if (!schedule.getRooms().contains(room)) {
            roomRepository.delete(room);
        }
    }

    // <========== PRIVATE ==========>
    // LẤY PHÒNG BẰNG ID
    private Room getRoomById(long roomId){
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng!"));
    }

}
