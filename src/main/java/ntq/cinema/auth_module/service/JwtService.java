package ntq.cinema.auth_module.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import ntq.cinema.auth_module.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secret;

    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(User user) {
        long now = System.currentTimeMillis();

        String roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getName().name())
                .reduce((r1, r2) -> r1 + "," + r2)
                .orElse("NO_ROLE");

        return Jwts.builder()
                .claim("username", user.getUsername())
                .claim("email", user.getEmail()) // Sử dụng email là username thực tế
                .claim("roles", roles)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(User user) {
        long now = System.currentTimeMillis();

        String roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getName().name())
                .reduce((r1, r2) -> r1 + "," + r2)
                .orElse("NO_ROLE");

        return Jwts.builder()
                .claim("roles", roles)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        // Dùng email làm username chính
        return claims.get("email", String.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        return email != null &&
                email.equals(userDetails.getUsername()) &&
                !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
