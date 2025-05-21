package ntq.cinema.schedule_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.movie_module.dto.response.movie.MovieResponse;
import ntq.cinema.schedule_module.dto.request.schedule.ScheduleCreateRequest;
import ntq.cinema.schedule_module.dto.request.schedule.ScheduleDeleteRequest;
import ntq.cinema.schedule_module.dto.request.schedule.ScheduleUpdateRequest;
import ntq.cinema.schedule_module.dto.response.schedule.ScheduleResponse;
import ntq.cinema.schedule_module.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.ntq-cinema-url}/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    // LẤY TẤT CẢ SUẤT CHIẾU CỦA PHIM
    @GetMapping("/by-movie")
    public ResponseEntity<?> getSchedulesByMovie(@RequestBody long movieId){
        try {
            List<ScheduleResponse> responses = scheduleService.getSchedulesByMovieId(movieId);
            return ResponseEntity.ok(responses);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // THÊM LỊCH CHIẾU CHO PHIM
    @PostMapping
    public ResponseEntity<?> createScheduleForMovie(@RequestBody ScheduleCreateRequest request){
        try {
            ScheduleResponse response = scheduleService.createScheduleForMovie(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // CẬP NHẬT LỊCH CHIẾU CHO PHIM
    @PutMapping
    public ResponseEntity<?> updateScheduleDateForMovie(ScheduleUpdateRequest request){
        try {
            ScheduleResponse response = scheduleService.updateScheduleDateForMovie(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // XÓA LỊCH CHIẾU CHO PHIM
    @DeleteMapping
    public ResponseEntity<?> deleteScheduleForMovie(ScheduleDeleteRequest request){
        try {
            scheduleService.deleteScheduleForMovie(request);
            return ResponseEntity.ok("Xóa lịch chiếu cho phim thành công!");
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }
}
