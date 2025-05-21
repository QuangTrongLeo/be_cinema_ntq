package ntq.cinema.schedule_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.movie_module.dto.response.movie.MovieResponse;
import ntq.cinema.schedule_module.dto.response.schedule.ScheduleResponse;
import ntq.cinema.schedule_module.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.ntq-cinema-url}/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

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
}
