package ntq.cinema.auth_module.config;

import ntq.cinema.auth_module.entity.Role;
import ntq.cinema.auth_module.enums.RoleTypeEnum;
import ntq.cinema.auth_module.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(RoleRepository roleRepository) {
        return args -> {
            try {
                if (roleRepository.count() == 0) {
                    roleRepository.save(new Role(RoleTypeEnum.ADMIN));  // Thêm role ADMIN
                    roleRepository.save(new Role(RoleTypeEnum.CUSTOMER));  // Thêm role CUSTOMER
                }
            } catch (Exception e) {
                System.err.println("Error loading roles: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
