package ntq.cinema.booking_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.booking_module.dto.request.seat.*;
import ntq.cinema.booking_module.dto.response.seat.SeatResponse;
import ntq.cinema.booking_module.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.ntq-cinema-url}/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    // LẤY CHỖ NGỒI
    @GetMapping("/get-seat")
    public ResponseEntity<SeatResponse> getSeatById(@RequestBody SeatGetIdRequest request) {
        SeatResponse response = seatService.getSeatById(request.getSeatId());
        return ResponseEntity.ok(response);
    }

    // THÊM CHỖ NGỒI
    @PostMapping
    public ResponseEntity<SeatResponse> createSeat(@RequestBody SeatCreateRequest request){
        SeatResponse response = seatService.createSeat(request);
        return ResponseEntity.ok(response);
    }

    // XÓA CHỖ NGỒI
    @DeleteMapping
    public ResponseEntity<String> deleteSeat(@RequestBody SeatDeleteRequest request){
        seatService.deleteSeat(request.getSeatId());
        return ResponseEntity.ok("Xóa chỗ ngồi thành công :)");
    }

    // CẬP NHẬT TÊN CHỖ NGỒI
    @PutMapping("/name")
    public ResponseEntity<SeatResponse> updateSeatName(@RequestBody SeatUpdateNameRequest request){
        SeatResponse response = seatService.updateSeatName(request);
        return ResponseEntity.ok(response);
    }

    // CẬP NHẬT GIÁ CHỖ NGỒI
    @PutMapping("/price")
    public ResponseEntity<SeatResponse> updateSeatPrice(@RequestBody SeatUpdatePriceRequest request){
        SeatResponse response = seatService.updateSeatPrice(request);
        return ResponseEntity.ok(response);
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI
    @PutMapping("/status")
    public ResponseEntity<SeatResponse> updateSeatStatus(@RequestBody SeatUpdateStatusRequest request){
        SeatResponse response = seatService.updateSeatStatus(request);
        return ResponseEntity.ok(response);
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI AVAILABLE CHO USER(SELECTING -> AVAILABLE)
    @PutMapping("/user/status/available")
    public ResponseEntity<SeatResponse> updateSeatStatusAvailable(@RequestBody SeatGetIdRequest request){
        SeatResponse response = seatService.updateSeatStatusAvailableForUser(request.getSeatId());
        return ResponseEntity.ok(response);
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI SELECTING CHO USER (AVAILABLE -> SELECTING)
    @PutMapping("/user/status/selecting")
    public ResponseEntity<SeatResponse> updateSeatStatusSelecting(@RequestBody SeatGetIdRequest request){
        SeatResponse response = seatService.updateSeatStatusSelectingForUser(request.getSeatId());
        return ResponseEntity.ok(response);
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI BOOKED CHO CUSTOMER (SELECTING -> CUSTOMER)
    @PutMapping("/cus/status/booked")
    public ResponseEntity<SeatResponse> updateSeatStatusBooked(@RequestBody SeatGetIdRequest request){
        SeatResponse response = seatService.updateSeatStatusBookedForCus(request.getSeatId());
        return ResponseEntity.ok(response);
    }
}
