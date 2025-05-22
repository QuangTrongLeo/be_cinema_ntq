package ntq.cinema.payment_module.config;

import ntq.cinema.booking_module.entity.SeatStatus;
import ntq.cinema.booking_module.enums.SeatStatusEnum;
import ntq.cinema.booking_module.repository.SeatStatusRepository;
import ntq.cinema.payment_module.entity.PaymentStatus;
import ntq.cinema.payment_module.enums.PaymentStatusEnum;
import ntq.cinema.payment_module.repository.PaymentStatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentStatusLoader {

    @Bean
    public CommandLineRunner loadPaymentStatus(PaymentStatusRepository paymentStatusRepository) {
        return args -> {
            for (PaymentStatusEnum statusEnum : PaymentStatusEnum.values()) {
                boolean exists = paymentStatusRepository.existsByStatus(statusEnum);
                if (!exists) {
                    paymentStatusRepository.save(new PaymentStatus(statusEnum));
                    System.out.println("Inserted missing payment status: " + statusEnum);
                }
            }
        };
    }
}
