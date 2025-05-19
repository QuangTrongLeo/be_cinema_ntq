package ntq.cinema.auth_module.repository;

import ntq.cinema.auth_module.entity.Role;
import ntq.cinema.auth_module.enums.RoleTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleTypeEnum name);
}
