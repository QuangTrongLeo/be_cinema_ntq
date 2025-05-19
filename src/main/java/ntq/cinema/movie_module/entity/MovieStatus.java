package ntq.cinema.movie_module.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntq.cinema.movie_module.enums.MovieStatusEnum;

// MovieStatus.java
@Entity
@Table(name = "movie_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statusId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieStatusEnum name;
}

