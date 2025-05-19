package ntq.cinema.movie_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.movie_module.dto.request.genre.GenreCreateRequest;
import ntq.cinema.movie_module.dto.request.genre.GenreDeleteRequest;
import ntq.cinema.movie_module.dto.request.genre.GenreUpdateRequest;
import ntq.cinema.movie_module.dto.response.genre.GenreResponse;
import ntq.cinema.movie_module.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.ntq-cinema-url}/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    // Lấy tất cả thể loại
    @GetMapping
    public ResponseEntity<List<GenreResponse>> getAllGenres() {
        List<GenreResponse> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    // Lấy thể loại theo ID
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getGenreById(@PathVariable("id") long id) {
        GenreResponse response = genreService.getGenreById(id);
        return ResponseEntity.ok(response);
    }

    // Tìm thể loại theo tên
    @GetMapping("/search")
    public ResponseEntity<GenreResponse> getGenreByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(genreService.getGenreByName(name));
    }

    // Tạo thể loại mới
    @PostMapping
    public ResponseEntity<GenreResponse> createGenre(@RequestBody GenreCreateRequest request) {
        GenreResponse response = genreService.createGenre(request);
        return ResponseEntity.ok(response);
    }

    // Cập nhật thể loại
    @PutMapping
    public ResponseEntity<GenreResponse> updateGenre(@RequestBody GenreUpdateRequest request) {
        GenreResponse response = genreService.updateGenre(request);
        return ResponseEntity.ok(response);
    }

    // Xoá thể loại
    @DeleteMapping
    public ResponseEntity<Void> deleteGenre(@RequestBody GenreDeleteRequest request) {
        genreService.deleteGenre(request.getGenreId());
        return ResponseEntity.noContent().build();
    }
}
