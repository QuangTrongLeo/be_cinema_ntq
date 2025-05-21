package ntq.cinema.schedule_module.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntq.cinema.movie_module.entity.Movie;

import java.time.LocalDate;
import java.util.List;

// Schedule.java
@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scheduleId;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Room> rooms;
}