package ntq.cinema.movie_module.service;

import jakarta.transaction.Transactional;
import ntq.cinema.movie_module.dto.request.genre.GenreCreateRequest;
import ntq.cinema.movie_module.dto.request.genre.GenreUpdateRequest;
import ntq.cinema.movie_module.dto.response.genre.GenreResponse;
import ntq.cinema.movie_module.entity.Genre;
import ntq.cinema.movie_module.mapper.GenreMapper;
import ntq.cinema.movie_module.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    // ========== 9.2 THỂ LOẠI PHIM ==========
    // Lấy tất cả thể loại
    public List<GenreResponse> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genreMapper.mapperToResponseList(genres);
    }

    // Lấy thể loại theo id
    public GenreResponse getGenreById(long genreId) {
        Genre genre = genreRepository.findById(genreId).orElse(null);
        return genreMapper.mapperToResponse(genre);
    }

    // Tìm thể loại theo tên
    public GenreResponse getGenreByName(String name) {
        Genre genre = genreRepository.findByName(name).orElse(null);
        return genreMapper.mapperToResponse(genre);
    }

    // Tạo thể loại phim
    public GenreResponse createGenre(GenreCreateRequest request) {
        Genre genre = new Genre();
        genre.setName(request.getGenreName());
        genre = genreRepository.save(genre);
        return genreMapper.mapperToResponse(genre);
    }

    // Cập nhật thể loại phim
    @Transactional
    public GenreResponse updateGenre(GenreUpdateRequest request) {
        Optional<Genre> genreOpt = genreRepository.findById(request.getGenreId());
        if (genreOpt.isEmpty()) {
            throw new RuntimeException("Không tìm thấy thể loại!");
        }

        Genre genre = genreOpt.get();
        genre.setName(request.getGenreName());
        return genreMapper.mapperToResponse(genre);
    }

    // Xóa thể loại phim
    public void deleteGenre(long genreId) {
        if (!genreRepository.existsById(genreId)) {
            throw new RuntimeException("Không tìm thấy thể loại!");
        }
        genreRepository.deleteById(genreId);
    }
}
