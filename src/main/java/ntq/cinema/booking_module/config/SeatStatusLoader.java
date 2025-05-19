package ntq.cinema.booking_module.config;

import ntq.cinema.booking_module.entity.SeatStatus;
import ntq.cinema.booking_module.enums.SeatStatusEnum;
import ntq.cinema.booking_module.repository.SeatStatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeatStatusLoader {

    @Bean
    public CommandLineRunner loadSeatStatus(SeatStatusRepository seatStatusRepository) {
        return args -> {
            if (seatStatusRepository.count() == 0) {
                seatStatusRepository.save(new SeatStatus(0L, SeatStatusEnum.BOOKED));
                seatStatusRepository.save(new SeatStatus(0L, SeatStatusEnum.SELECTING));
                seatStatusRepository.save(new SeatStatus(0L, SeatStatusEnum.AVAILABLE));
            }
        };
    }
}
