package ntq.cinema.schedule_module.repository;

import ntq.cinema.schedule_module.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findById(Long id);
    boolean existsByName(String name);
}
