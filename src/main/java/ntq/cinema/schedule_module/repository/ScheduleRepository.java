package ntq.cinema.schedule_module.repository;

import ntq.cinema.movie_module.entity.Movie;
import ntq.cinema.schedule_module.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // LẤY TẤT CẢ XUẤT CHIẾU BẰNG ĐỐI TƯỢNG PHIM
    List<Schedule> findAllByMovie(Movie movie);
}
