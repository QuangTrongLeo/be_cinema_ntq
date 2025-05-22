package ntq.cinema.payment_module.repository;

import ntq.cinema.payment_module.entity.PaymentStatus;
import ntq.cinema.payment_module.enums.PaymentStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    boolean existsByStatus(PaymentStatusEnum status);

    Optional<PaymentStatus> findByStatus(PaymentStatusEnum status);
}
