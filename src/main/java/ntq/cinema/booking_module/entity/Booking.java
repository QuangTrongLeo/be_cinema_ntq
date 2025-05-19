package ntq.cinema.booking_module.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntq.cinema.auth_module.entity.User;
import ntq.cinema.schedule_module.entity.ShowTime;
import java.sql.Timestamp;

// Booking.java
@Entity
@Table(name = "booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private ShowTime showTime;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
}