package ntq.cinema.movie_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.movie_module.dto.request.movie.MovieCreateRequest;
import ntq.cinema.movie_module.dto.response.movie.MovieResponse;
import ntq.cinema.movie_module.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.ntq-cinema-url}/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    // LẤY TẤT CẢ PHIM
    @GetMapping("/all")
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        List<MovieResponse> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    // DANH SÁCH PHIM UPCOMING
    @GetMapping("/up-coming")
    public ResponseEntity<?> getMoviesStatusUpComing(){
        try {
            List<MovieResponse> movies = movieService.getMoviesStatusUpComing();
            return ResponseEntity.ok(movies);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // DANH SÁCH PHIM NOWSHOWING
    @GetMapping("/now-showing")
    public ResponseEntity<?> getMoviesStatusNowShowing(){
        try {
            List<MovieResponse> movies = movieService.getMoviesStatusNowShowing();
            return ResponseEntity.ok(movies);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // DANH SÁCH PHIM STOPPED
    @GetMapping("/stopped")
    public ResponseEntity<?> getMoviesStatusStopped(){
        try {
            List<MovieResponse> movies = movieService.getMoviesStatusNowShowing();
            return ResponseEntity.ok(movies);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // LẤY CÁC PHIM BẰNG THỂ LOẠI PHIM
    @GetMapping
    public ResponseEntity<?> getMoviesByGenreName(@RequestParam String genre) {
        try {
            List<MovieResponse> movies = movieService.searchMoviesByGenreName(genre);
            return ResponseEntity.ok(movies);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            // Bắt các lỗi bất ngờ khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // LẤY CÁC PHIM BẰNG TỪ KHÓA
    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(@RequestParam String searched){
        try {
            List<MovieResponse> movies = movieService.searchMovies(searched);
            return ResponseEntity.ok(movies);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            // Bắt các lỗi bất ngờ khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // TẠO PHIM MỚI
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMovie(@ModelAttribute MovieCreateRequest request){
        try {
            MovieResponse movie = movieService.createMovie(request);
            return ResponseEntity.ok(movie);
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

}
