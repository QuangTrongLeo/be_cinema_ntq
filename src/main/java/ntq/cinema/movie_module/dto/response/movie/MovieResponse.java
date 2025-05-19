package ntq.cinema.movie_module.dto.response.movie;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse {
    private long movieId;
    private String title;
    private int duration;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate releaseDate;
    private String director;
    private String description;
    private String posterUrl;
    private String genreName;        // Lấy tên thể loại từ Genre
    private String statusName;       // Lấy tên trạng thái từ MovieStatusEnum
}
