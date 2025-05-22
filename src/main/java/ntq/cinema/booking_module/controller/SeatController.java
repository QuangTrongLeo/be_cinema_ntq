package ntq.cinema.booking_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.booking_module.dto.request.seat.*;
import ntq.cinema.booking_module.dto.response.booking.BookingResponse;
import ntq.cinema.booking_module.dto.response.seat.SeatResponse;
import ntq.cinema.booking_module.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.ntq-cinema-url}/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    // LẤY CHỖ NGỒI
    @GetMapping("/get-seat")
    public ResponseEntity<?> getSeatById(@RequestBody SeatGetIdRequest request) {
        try {
            SeatResponse response = seatService.getSeatById(request.getSeatId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // THÊM CHỖ NGỒI
    @PostMapping
    public ResponseEntity<?> createSeat(@RequestBody SeatCreateRequest request){
        try {
            SeatResponse response = seatService.createSeat(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // XÓA CHỖ NGỒI
    @DeleteMapping
    public ResponseEntity<?> deleteSeat(@RequestBody SeatDeleteRequest request){
        try {
            seatService.deleteSeat(request.getSeatId());
            return ResponseEntity.ok("Xóa chỗ ngồi thành công :)");
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // CẬP NHẬT TÊN CHỖ NGỒI
    @PutMapping("/name")
    public ResponseEntity<?> updateSeatName(@RequestBody SeatUpdateNameRequest request){
        try {
            SeatResponse response = seatService.updateSeatName(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // CẬP NHẬT GIÁ CHỖ NGỒI
    @PutMapping("/price")
    public ResponseEntity<?> updateSeatPrice(@RequestBody SeatUpdatePriceRequest request){
        try {
            SeatResponse response = seatService.updateSeatPrice(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI
    @PutMapping("/status")
    public ResponseEntity<?> updateSeatStatus(@RequestBody SeatUpdateStatusRequest request){
        try {
            SeatResponse response = seatService.updateSeatStatus(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI AVAILABLE CHO USER(SELECTING -> AVAILABLE)
    @PutMapping("/user/status/available")
    public ResponseEntity<?> updateSeatStatusAvailable(@RequestBody SeatGetIdRequest request){
        try {
            SeatResponse response = seatService.updateSeatStatusAvailableForUser(request.getSeatId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI SELECTING CHO USER (AVAILABLE -> SELECTING)
    @PutMapping("/user/status/selecting")
    public ResponseEntity<?> updateSeatStatusSelecting(@RequestBody SeatGetIdRequest request){
        try {
            SeatResponse response = seatService.updateSeatStatusSelectingForUser(request.getSeatId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI BOOKED CHO CUSTOMER (SELECTING -> CUSTOMER)
    @PutMapping("/cus/status/booked")
    public ResponseEntity<?> updateSeatStatusBooked(@RequestBody SeatGetIdRequest request){
        try {
            SeatResponse response = seatService.updateSeatStatusBookedForCus(request.getSeatId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }
}
