package ntq.cinema.movie_module.dto.request.genre;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GenreUpdateRequest {
    private long genreId;
    private String genreName;
}
