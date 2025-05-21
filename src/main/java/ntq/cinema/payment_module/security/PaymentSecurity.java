package ntq.cinema.payment_module.security;

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
public class PaymentSecurity {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${api.ntq-cinema-url}")
    private String cinemaApiBaseUrl;

    private static final String PAYMENT_API = "/payments";

    @Autowired
    public PaymentSecurity(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChainPayment(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(cinemaApiBaseUrl + PAYMENT_API + "/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, cinemaApiBaseUrl + PAYMENT_API + "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, cinemaApiBaseUrl + PAYMENT_API + "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, cinemaApiBaseUrl + PAYMENT_API + "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, cinemaApiBaseUrl + PAYMENT_API + "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManagerPayment(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
