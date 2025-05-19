package ntq.cinema.booking_module.repository;

import ntq.cinema.booking_module.entity.SeatStatus;
import ntq.cinema.booking_module.enums.SeatStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatStatusRepository extends JpaRepository<SeatStatus, Long> {
    Optional<SeatStatus> findByName(SeatStatusEnum name);
}
