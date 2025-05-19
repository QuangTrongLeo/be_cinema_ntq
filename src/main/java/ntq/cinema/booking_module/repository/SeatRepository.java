package ntq.cinema.booking_module.repository;

import ntq.cinema.booking_module.entity.Seat;
import ntq.cinema.schedule_module.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByRoom(Room room);

    void deleteByRoom_RoomId(long roomId);
}
