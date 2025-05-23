package ntq.cinema.payment_module.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntq.cinema.booking_module.entity.Booking;
import ntq.cinema.payment_module.enums.PaymentStatusEnum;

import java.sql.Timestamp;

// Payment.java
@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "payment_gateway", nullable = false)
    private String paymentGateway;

    @Column(nullable = false)
    private int amount;

    @Column(name = "payment_time")
    private Timestamp paymentTime;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private PaymentStatus status;

    @PrePersist
    protected void onPayment() {
        if (paymentTime == null) {
            this.paymentTime = new Timestamp(System.currentTimeMillis());
        }
    }
}


