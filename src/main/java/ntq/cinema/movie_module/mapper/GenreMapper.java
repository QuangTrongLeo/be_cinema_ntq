package ntq.cinema.movie_module.mapper;

import ntq.cinema.movie_module.dto.response.genre.GenreResponse;
import ntq.cinema.movie_module.entity.Genre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreMapper {

    public GenreResponse mapperToResponse(Genre genre) {
        if (genre == null) {
            return null;
        }

        return GenreResponse.builder()
                .genreId(genre.getGenreId())
                .genreName(genre.getName())
                .build();
    }

    // Chuyển đổi danh sách Genre sang danh sách GenreResponse
    public List<GenreResponse> mapperToResponseList(List<Genre> genres) {
        return genres.stream()
                .map(this::mapperToResponse)  // Chuyển đổi từng Genre sang GenreResponse
                .collect(Collectors.toList());
    }
}
