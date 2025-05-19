package ntq.cinema.movie_module.repository;

import ntq.cinema.movie_module.entity.MovieStatus;
import ntq.cinema.movie_module.enums.MovieStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.lang.ScopedValue;
import java.util.Optional;

public interface MovieStatusRepository extends JpaRepository<MovieStatus, Long> {
    Optional<MovieStatus> findByName(MovieStatusEnum movieStatusEnum);
}
