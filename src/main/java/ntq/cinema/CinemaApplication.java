package ntq.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CinemaApplication {

	public static void main(String[] args) {

		// Khởi động Spring Boot
		SpringApplication.run(CinemaApplication.class, args);
	}
}
