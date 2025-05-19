package ntq.cinema.booking_module.entity;

import jakarta.persistence.*;
import lombok.*;
import ntq.cinema.schedule_module.entity.Room;

@Entity
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seatId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private SeatStatus status;
}
