package ntq.cinema.movie_module.dto.response.genre;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenreResponse {
    private long genreId;
    private String genreName;
}
