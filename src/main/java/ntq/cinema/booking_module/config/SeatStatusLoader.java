package ntq.cinema.booking_module.config;

import ntq.cinema.booking_module.entity.SeatStatus;
import ntq.cinema.booking_module.enums.SeatStatusEnum;
import ntq.cinema.booking_module.repository.SeatStatusRepository;
import ntq.cinema.movie_module.entity.MovieStatus;
import ntq.cinema.movie_module.enums.MovieStatusEnum;
import ntq.cinema.movie_module.repository.MovieStatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeatStatusLoader {

    @Bean
    public CommandLineRunner loadSeatStatus(SeatStatusRepository seatStatusRepository) {
        return args -> {
            for (SeatStatusEnum statusEnum : SeatStatusEnum.values()) {
                boolean exists = seatStatusRepository.existsByStatus(statusEnum);
                if (!exists) {
                    seatStatusRepository.save(new SeatStatus(statusEnum));
                    System.out.println("Inserted missing seat status: " + statusEnum);
                }
            }
        };
    }
}
