package ntq.cinema.schedule_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.schedule_module.dto.request.room.RoomCreateRequest;
import ntq.cinema.schedule_module.dto.request.room.RoomDeleteRequest;
import ntq.cinema.schedule_module.dto.request.room.RoomGetIdRequest;
import ntq.cinema.schedule_module.dto.request.room.RoomUpdateRequest;
import ntq.cinema.schedule_module.dto.response.room.RoomResponse;
import ntq.cinema.schedule_module.dto.response.room.RoomWithSeatsResponse;
import ntq.cinema.schedule_module.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.ntq-cinema-url}/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    // DANH SÁCH PHÒNG CHIẾU
    @GetMapping("/all")
    public ResponseEntity<List<RoomResponse>> getAllRooms(){
        List<RoomResponse> responses = roomService.getAllRooms();
        return ResponseEntity.ok(responses);
    }

    // CHỌN PHÒNG CHIẾU
    @GetMapping("/get-room")
    public ResponseEntity<RoomWithSeatsResponse> getRoomByRoomId(@RequestBody RoomGetIdRequest request){
        RoomWithSeatsResponse response = roomService.getRoomByRoomId(request.getRoomId());
        return ResponseEntity.ok(response);
    }

    // TẠO PHÒNG CHIẾU
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomCreateRequest request){
        RoomResponse response = roomService.createRoom(request);
        return ResponseEntity.ok(response);
    }

    // CẬP NHẬT (TÊN) PHÒNG CHIẾU
    @PutMapping
    public ResponseEntity<RoomResponse> updateRoom(@RequestBody RoomUpdateRequest request){
        RoomResponse response = roomService.updateRoom(request);
        return ResponseEntity.ok(response);
    }

    // XÓA PHÒNG CHIẾU
    @DeleteMapping
    public ResponseEntity<String> deleteRoom(@RequestBody RoomDeleteRequest request){
        roomService.deleteRoom(request.getRoomId());
        return ResponseEntity.ok("Đã xóa phòng thành công!");
    }
}
