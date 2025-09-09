package com.cmms.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfig corsConfig) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> corsConfig.corsFilter())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/ws/**").permitAll() // WebSocket endpoints
                        .requestMatchers("/api/ws/**").permitAll() // SockJS endpoints  
                        .requestMatchers("/sockjs-node/**").permitAll() // SockJS endpoints for development
                        .requestMatchers("/api/tag-history/**").permitAll() // Tag history endpoints
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/assets/**").hasAnyRole("ADMIN", "MANAGER", "TECHNICIAN")
                        .requestMatchers("/api/asset-types/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/locations/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/tenants/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/suppliers/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/inventories/**").hasAnyRole("ADMIN", "MANAGER", "TECHNICIAN")
                        .requestMatchers("/api/iot-sensors/**").hasAnyRole("ADMIN", "MANAGER", "TECHNICIAN")
                        .requestMatchers("/api/preventive-maintenance/**").hasAnyRole("ADMIN", "MANAGER", "TECHNICIAN")
                        .requestMatchers("/api/work-orders/**").hasAnyRole("ADMIN", "MANAGER", "TECHNICIAN")
                        .requestMatchers("/api/reports/**").hasAnyRole("ADMIN", "MANAGER", "TECHNICIAN")
                        .requestMatchers("/api/documents/**").hasAnyRole("ADMIN", "MANAGER", "TECHNICIAN")
                        .requestMatchers("/api/maintenance-history/**").hasAnyRole("ADMIN", "MANAGER", "TECHNICIAN")
                        .requestMatchers("/api/notifications/**").hasAnyRole("ADMIN", "MANAGER", "TECHNICIAN")
                        .requestMatchers("api/work-order-parts/**").hasAnyRole("ADMIN", "MANAGER", "TECHNICIAN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
