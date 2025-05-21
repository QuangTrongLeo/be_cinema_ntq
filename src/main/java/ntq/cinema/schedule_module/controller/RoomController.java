package ntq.cinema.schedule_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.schedule_module.dto.request.room.RoomCreateRequest;
import ntq.cinema.schedule_module.dto.request.room.RoomDeleteRequest;
import ntq.cinema.schedule_module.dto.request.room.RoomGetIdRequest;
import ntq.cinema.schedule_module.dto.request.room.RoomUpdateRequest;
import ntq.cinema.schedule_module.dto.response.room.RoomResponse;
import ntq.cinema.schedule_module.dto.response.room.RoomWithSeatsResponse;
import ntq.cinema.schedule_module.dto.response.schedule.ScheduleResponse;
import ntq.cinema.schedule_module.service.RoomService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getAllRooms(){
        try {
            List<RoomResponse> responses = roomService.getAllRooms();
            return ResponseEntity.ok(responses);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // CHỌN PHÒNG CHIẾU
    @GetMapping("/get-room")
    public ResponseEntity<?> getRoomByRoomId(@RequestBody RoomGetIdRequest request){
        try {
            RoomWithSeatsResponse response = roomService.getRoomByRoomId(request.getRoomId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // TẠO PHÒNG CHIẾU
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomCreateRequest request){
        try {
            RoomResponse response = roomService.createRoom(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // CẬP NHẬT (TÊN) PHÒNG CHIẾU
    @PutMapping
    public ResponseEntity<?> updateRoom(@RequestBody RoomUpdateRequest request){
        try {
            RoomResponse response = roomService.updateRoom(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // XÓA PHÒNG CHIẾU
    @DeleteMapping
    public ResponseEntity<?> deleteRoom(@RequestBody RoomDeleteRequest request){
        try {
            roomService.deleteRoom(request.getRoomId());
            return ResponseEntity.ok("Đã xóa phòng thành công!");
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }
}
