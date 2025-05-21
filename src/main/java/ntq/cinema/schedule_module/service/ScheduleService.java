package ntq.cinema.schedule_module.service;

import ntq.cinema.movie_module.entity.Movie;
import ntq.cinema.movie_module.repository.MovieRepository;
import ntq.cinema.schedule_module.dto.request.schedule.ScheduleCreateRequest;
import ntq.cinema.schedule_module.dto.response.schedule.ScheduleResponse;
import ntq.cinema.schedule_module.entity.Schedule;
import ntq.cinema.schedule_module.mapper.ScheduleMapper;
import ntq.cinema.schedule_module.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final ScheduleMapper scheduleMapper;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           MovieRepository movieRepository,
                           ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.movieRepository = movieRepository;
        this.scheduleMapper = scheduleMapper;
    }

    // LẤY TẤT CẢ LỊCH CHIẾU CỦA PHIM BẰNG ID CỦA PHIM
    public List<ScheduleResponse> getSchedulesByMovieId(long movieId) {
        Movie movie = getMovieById(movieId);
        List<Schedule> schedules = getSchedulesByMovie(movie);
        return scheduleMapper.mapperToResponseList(schedules);
    }

    // THÊM LỊCH CHIẾU CHO PHIM
    public ScheduleResponse createScheduleForMovie(ScheduleCreateRequest request){
        Movie movie = getMovieById(request.getMovieId());
        Schedule schedule = new Schedule();
        schedule.setMovie(movie);
        schedule.setDate(request.getDate());
        scheduleRepository.save(schedule);
        return scheduleMapper.mapperToResponse(schedule);
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
