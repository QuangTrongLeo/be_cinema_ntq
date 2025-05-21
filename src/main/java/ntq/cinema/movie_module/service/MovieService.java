package ntq.cinema.movie_module.service;

import ntq.cinema.movie_module.dto.request.movie.MovieCreateRequest;
import ntq.cinema.movie_module.dto.response.movie.MovieResponse;
import ntq.cinema.movie_module.entity.Genre;
import ntq.cinema.movie_module.entity.Movie;
import ntq.cinema.movie_module.entity.MovieStatus;
import ntq.cinema.movie_module.enums.MovieStatusEnum;
import ntq.cinema.movie_module.mapper.MovieMapper;
import ntq.cinema.movie_module.repository.GenreRepository;
import ntq.cinema.movie_module.repository.MovieRepository;
import ntq.cinema.movie_module.repository.MovieStatusRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieStatusRepository movieStatusRepository;
    private final GenreRepository genreRepository;
    private final MovieMapper movieMapper;
    private final S3Service s3Service;

    public MovieService(MovieRepository movieRepository,
                        MovieStatusRepository movieStatusRepository,
                        GenreRepository genreRepository,
                        MovieMapper movieMapper,
                        S3Service s3Service) {
        this.movieRepository = movieRepository;
        this.movieStatusRepository = movieStatusRepository;
        this.genreRepository = genreRepository;
        this.movieMapper = movieMapper;
        this.s3Service = s3Service;
    }

    // DANH SÁCH TẤT CẢ CÁC PHIM
    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findAllByOrderByMovieIdDesc();
        return movieMapper.mapperToResponseList(movies);
    }

    // DANH SÁCH PHIM SẮP CHIẾU(UPCOMING)
    public List<MovieResponse> getMoviesStatusUpComing() {
        MovieStatus movieStatus = findMovieStatusIsUpComing();
        List<Movie> movies = getMoviesByMovieStatus(movieStatus);
        return movieMapper.mapperToResponseList(movies);
    }

    // DANH SÁCH PHIM ĐANG CHIẾU(NOW_SHOWING)
    public List<MovieResponse> getMoviesStatusNowShowing() {
        MovieStatus movieStatus = findMovieStatusIsNowShowing();
        List<Movie> movies = getMoviesByMovieStatus(movieStatus);
        return movieMapper.mapperToResponseList(movies);
    }

    // DANH SÁCH PHIM ĐANG CHIẾU(NOW_SHOWING)
    public List<MovieResponse> getMoviesStatusStopped() {
        MovieStatus movieStatus = findMovieStatusIsStopped();
        List<Movie> movies = getMoviesByMovieStatus(movieStatus);
        return movieMapper.mapperToResponseList(movies);
    }


    //4.1. Tìm kiếm phim theo tên
    public List<MovieResponse> searchMovies(String title){
        List<Movie> movies;

        //4.1.3. Kiểm tra nếu tiêu đề chưa có hoặc rỗng
        if (title == null || title.trim().isEmpty()) {
            //4.1.4. Nếu không có tên phim, lấy toàn bộ danh sách phim sắp xếp gia dần theo movieId
            movies = movieRepository.findAllByOrderByMovieIdDesc();
        } else {
            //4.1.5. Nếu có tên phim, tìm kiếm tên phim có chứa ký tự tìm kiếm (không phân biệt hoa thường) và sắp xếp giảm dần
            movies = movieRepository.findByTitleContainingIgnoreCaseOrderByMovieIdDesc(title);
        }
        //4.1.6. Chuyển danh sách entity movie thành movieResponse để trả ra ngoài
        return movieMapper.mapperToResponseList(movies);
    }

    // 4.2 Lọc phim theo thể loại
    public List<MovieResponse> searchMoviesByGenreName(String nameGenre) {
        // 4.2.3 Tìm thể loại theo tên từ genreRepository
        Genre genre = genreRepository.findByName(nameGenre).orElse(null);
        // 4.2.4  Tìm danh sách phim thuộc thể loại, sắp xếp theo MovieId giảm dần
        List<Movie> movies = movieRepository.findByGenreOrderByMovieIdDesc(genre);
        // 4.2.5 Chuyển danh sách entity movie thành movieReponse để trả ra ngoài
        return movieMapper.mapperToResponseList(movies);
    }


    // TẠO PHIM MỚI
    public MovieResponse createMovie(MovieCreateRequest request){

        LocalDate today = LocalDate.now();
        if (request.getReleaseDate().isBefore(today)) {
            throw new RuntimeException("Ngày phát hành không được nằm trong quá khứ!");
        }

        String posterUrl = s3Service.uploadFile(request.getPosterFile());

        Genre genre = findGenreByGenreId(request.getGenreId());

        MovieStatus movieStatus = findMovieStatusIsUpComing();

        // Tạo movie mới
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDuration(request.getDuration());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setDirector(request.getDirector());
        movie.setDescription(request.getDescription());
        movie.setGenre(genre);
        movie.setStatus(movieStatus);
        movie.setPosterUrl(posterUrl);

        // Tạo suất chiếu


        // Lưu và trả response
        movieRepository.save(movie);
        return movieMapper.mapperToResponse(movie);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateMovieStatusToNowShowing(){
        LocalDate today = LocalDate.now();

        // Lấy danh sách phim có releaseDate là hôm nay và đang có status là UPCOMING
        List<Movie> moviesToUpdate = movieRepository.findByReleaseDateAndStatus(today, MovieStatusEnum.UPCOMING);

        if (moviesToUpdate.isEmpty()) return;

        // Lấy trạng thái NOW_SHOWING
        MovieStatus nowShowingStatus = findMovieStatusIsNowShowing();

        for (Movie movie : moviesToUpdate) {
            movie.setStatus(nowShowingStatus);
        }

        // Cập nhật sang trạng thái NOW_SHOWING cho tất cả các phim tới ngày releaseDate
        movieRepository.saveAll(moviesToUpdate);
    }

    // Lấy Genre theo ID của genre
    private Genre findGenreByGenreId(long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thể loại!"));
    }

    // Lấy Genre theo name của genre
    private Genre findGenreByGenreName(String genreName) {
        return genreRepository.findByName(genreName)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thể loai phim!"));
    }

    // Lấy MovieStatus với trạng thái UPCOMING
    private MovieStatus findMovieStatusIsUpComing(){
        return movieStatusRepository.findByStatus(MovieStatusEnum.UPCOMING)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái UPCOMING!"));
    }

    // Lấy MovieStatus với trạng thái NOW_SHOWING
    private MovieStatus findMovieStatusIsNowShowing(){
        return movieStatusRepository.findByStatus(MovieStatusEnum.NOW_SHOWING)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái NOW_COMING!"));
    }

    // Lấy MovieStatus với trạng thái NOW_SHOWING
    private MovieStatus findMovieStatusIsStopped(){
        return movieStatusRepository.findByStatus(MovieStatusEnum.STOPPED)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái STOPPED!"));
    }

    // LẤY DANH SÁCH PHIM BẰNG TRẠNG THÁI PHIM
    private List<Movie> getMoviesByMovieStatus(MovieStatus movieStatus){
        return movieRepository.findByStatus(movieStatus);
    }

}
