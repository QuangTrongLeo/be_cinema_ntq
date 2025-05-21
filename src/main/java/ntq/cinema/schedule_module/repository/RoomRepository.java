package ntq.cinema.schedule_module.repository;

import ntq.cinema.schedule_module.entity.Room;
import ntq.cinema.schedule_module.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findById(Long id);
    boolean existsByName(String name);

    List<Room> findAllBySchedule(Schedule schedule);
}
