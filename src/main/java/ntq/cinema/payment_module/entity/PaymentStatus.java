package ntq.cinema.payment_module.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntq.cinema.payment_module.enums.PaymentStatusEnum;

@Entity
@Table(name = "payment_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statusId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PaymentStatusEnum status;

    public PaymentStatus(PaymentStatusEnum status) {
        this.status = status;
    }
}

