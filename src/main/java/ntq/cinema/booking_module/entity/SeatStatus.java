package ntq.cinema.booking_module.entity;

import jakarta.persistence.*;
import lombok.*;
import ntq.cinema.booking_module.enums.SeatStatusEnum;

@Entity
@Table(name = "seat_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statusId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatusEnum name;
}
