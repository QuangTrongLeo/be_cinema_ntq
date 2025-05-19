package ntq.cinema.auth_module.mapper;

import ntq.cinema.auth_module.dto.response.JwtResponse;
import org.springframework.stereotype.Component;

@Component
public class JwtMapper {
    public JwtResponse toJwtResponse(String accessToken, String refreshToken) {
        return new JwtResponse(accessToken, refreshToken);
    }
}
