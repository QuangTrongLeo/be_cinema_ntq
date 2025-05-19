package ntq.cinema.movie_module.dto.request.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class MovieCreateRequest {
    private String title;
    private int duration;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate releaseDate;
    private String director;
    private String description;
    private Long genreId;
    private MultipartFile posterFile;
}
