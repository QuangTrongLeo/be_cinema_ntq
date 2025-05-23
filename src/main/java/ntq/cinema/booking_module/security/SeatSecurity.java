package ntq.cinema.booking_module.security;

import ntq.cinema.auth_module.config.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SeatSecurity {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${api.ntq-cinema-url}")
    private String cinemaApiBaseUrl;

    private static final String SEAT_API = "/seats";

    @Autowired
    public SeatSecurity(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChainSeat(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(cinemaApiBaseUrl + SEAT_API + "/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, cinemaApiBaseUrl + SEAT_API + "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, cinemaApiBaseUrl + SEAT_API + "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, cinemaApiBaseUrl + SEAT_API + "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, cinemaApiBaseUrl + SEAT_API + "/cus/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, cinemaApiBaseUrl + SEAT_API + "/user/**").permitAll()
                        .requestMatchers(HttpMethod.GET, cinemaApiBaseUrl + SEAT_API + "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManagerSeat(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
