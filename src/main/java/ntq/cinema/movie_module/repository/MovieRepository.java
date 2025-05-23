package ntq.cinema.movie_module.repository;

import ntq.cinema.movie_module.entity.Genre;
import ntq.cinema.movie_module.entity.Movie;
import ntq.cinema.movie_module.entity.MovieStatus;
import ntq.cinema.movie_module.enums.MovieStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByOrderByMovieIdDesc();

    List<Movie> findByTitleContainingIgnoreCaseOrderByMovieIdDesc(String title);

    List<Movie> findAllByGenre_NameContaining(String genreName);

    List<Movie> findByGenreOrderByMovieIdDesc(Genre genre);

    List<Movie> findByReleaseDateAndStatus(LocalDate today, MovieStatusEnum movieStatusEnum);

    List<Movie> findByStatus(MovieStatus movieStatus);
}
