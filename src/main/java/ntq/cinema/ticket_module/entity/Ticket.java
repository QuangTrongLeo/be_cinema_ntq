package ntq.cinema.ticket_module.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntq.cinema.booking_module.entity.Booking;

import java.sql.Timestamp;

// Ticket.java
@Entity
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Column(name = "sent_to_email")
    private boolean sentToEmail;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @PrePersist
    protected void onCreate() {
        this.createdTime = new Timestamp(System.currentTimeMillis());
    }
}
