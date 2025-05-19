package ntq.cinema.movie_module.mapper;

import ntq.cinema.movie_module.dto.response.genre.GenreResponse;
import ntq.cinema.movie_module.dto.response.movie.MovieResponse;
import ntq.cinema.movie_module.entity.Genre;
import ntq.cinema.movie_module.entity.Movie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapper {

    public MovieResponse mapperToResponse(Movie movie) {
        if (movie == null) {
            return null;
        }

        return MovieResponse.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .duration(movie.getDuration())
                .releaseDate(movie.getReleaseDate())
                .director(movie.getDirector())
                .description(movie.getDescription())
                .posterUrl(movie.getPosterUrl())
                .genreName(movie.getGenre() != null ? movie.getGenre().getName() : null)
                .statusName(movie.getStatus() != null ? movie.getStatus().getName().name() : null)
                .build();
    }

    public List<MovieResponse> mapperToResponseList(List<Movie> movieses) {
        return movieses.stream()
                .map(this::mapperToResponse)  // Chuyển đổi từng Genre sang GenreResponse
                .collect(Collectors.toList());
    }
}
