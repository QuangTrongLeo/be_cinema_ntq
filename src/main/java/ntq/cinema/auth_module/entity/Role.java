package ntq.cinema.auth_module.entity;

import jakarta.persistence.*;
import lombok.*;
import ntq.cinema.auth_module.enums.RoleTypeEnum;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleTypeEnum name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles = new ArrayList<>();

    public Role(RoleTypeEnum name) {
        this.name = name;
    }
}
