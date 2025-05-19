package ntq.cinema.movie_module.config;

import org.springframework.boot.CommandLineRunner;
import ntq.cinema.movie_module.entity.MovieStatus;
import ntq.cinema.movie_module.enums.MovieStatusEnum;
import ntq.cinema.movie_module.repository.MovieStatusRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovieStatusLoader {

    @Bean
    public CommandLineRunner loadMovieStatus(MovieStatusRepository movieStatusRepository) {
        return args -> {
            if (movieStatusRepository.count() == 0) {
                movieStatusRepository.save(new MovieStatus(0L, MovieStatusEnum.UPCOMING));
                movieStatusRepository.save(new MovieStatus(0L, MovieStatusEnum.NOW_SHOWING));
            }
        };
    }
}
