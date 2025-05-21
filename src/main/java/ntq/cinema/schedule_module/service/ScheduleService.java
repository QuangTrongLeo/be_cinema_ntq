package ntq.cinema.schedule_module.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ntq.cinema.movie_module.entity.Movie;
import ntq.cinema.movie_module.repository.MovieRepository;
import ntq.cinema.schedule_module.dto.request.schedule.ScheduleCreateRequest;
import ntq.cinema.schedule_module.dto.request.schedule.ScheduleDeleteRequest;
import ntq.cinema.schedule_module.dto.request.schedule.ScheduleUpdateRequest;
import ntq.cinema.schedule_module.dto.response.schedule.ScheduleResponse;
import ntq.cinema.schedule_module.entity.Schedule;
import ntq.cinema.schedule_module.mapper.ScheduleMapper;
import ntq.cinema.schedule_module.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final ScheduleMapper scheduleMapper;

    // LẤY TẤT CẢ LỊCH CHIẾU CỦA PHIM BẰNG ID CỦA PHIM
    public List<ScheduleResponse> getSchedulesByMovieId(long movieId) {
        Movie movie = getMovieById(movieId);
        List<Schedule> schedules = getSchedulesByMovie(movie);
        return scheduleMapper.mapperToResponseList(schedules);
    }

    // THÊM LỊCH CHIẾU CHO PHIM
    @Transactional
    public ScheduleResponse createScheduleForMovie(ScheduleCreateRequest request){
        Movie movie = getMovieById(request.getMovieId());
        Schedule schedule = new Schedule();
        schedule.setMovie(movie);
        schedule.setDate(request.getDate());
        scheduleRepository.save(schedule);
        return scheduleMapper.mapperToResponse(schedule);
    }

    // CẬP NHẬT LỊCH CHIẾU CHO PHIM
    @Transactional
    public ScheduleResponse updateScheduleDateForMovie(ScheduleUpdateRequest request){
        Movie movie = getMovieById(request.getMovieId());
        Schedule schedule = getScheduleById(request.getScheduleId());
        // Nếu muốn đảm bảo lịch chiếu thuộc về phim này, có thể kiểm tra thêm:
        if (schedule.getMovie() == null || schedule.getMovie().getMovieId() != movie.getMovieId()) {
            throw new RuntimeException("Lịch chiếu không thuộc phim này!");
        }

        schedule.setDate(request.getDate());
        scheduleRepository.save(schedule);
        return scheduleMapper.mapperToResponse(schedule);
    }

    // XÓA LỊCH CHIẾU CHO PHIM
    @Transactional
    public void deleteScheduleForMovie(ScheduleDeleteRequest request) {
        Movie movie = getMovieById(request.getMovieId());
        Schedule schedule = getScheduleById(request.getScheduleId());
        // Nếu muốn đảm bảo lịch chiếu thuộc về phim này, có thể kiểm tra thêm:
        if (schedule.getMovie() == null || schedule.getMovie().getMovieId() != movie.getMovieId()) {
            throw new RuntimeException("Lịch chiếu không thuộc phim này!");
        }
        scheduleRepository.delete(schedule);
    }

    // LẤY LỊCH CHIẾU BẰNG ID
    public Schedule getScheduleById(long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch chiếu!"));
    }

    // LẤY PHIM BẰNG ID
    private Movie getMovieById(long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim!"));
    }

    // LẤY TẤT CẢ CÁC LỊCH CHIẾU BẰNG PHIM
    private List<Schedule> getSchedulesByMovie(Movie movie) {
        return scheduleRepository.findAllByMovie(movie);
    }


}
