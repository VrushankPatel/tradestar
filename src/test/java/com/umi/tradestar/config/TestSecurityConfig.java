package com.umi.tradestar.config;

import com.umi.tradestar.model.User;
import com.umi.tradestar.model.enums.Role;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Test security configuration that provides a simplified security setup for testing.
 * This configuration bypasses Keycloak and uses only JWT authentication.
 */
@TestConfiguration
@EnableWebSecurity
@EnableMethodSecurity
public class TestSecurityConfig {

    /**
     * Mock UserDetailsService for testing
     */
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return username -> {
            if ("test@example.com".equals(username)) {
                return User.builder()
                        .id(1L)
                        .email(username)
                        .firstName("Test")
                        .lastName("User")
                        .role(Role.TRADER)
                        .password("$2a$10$test_encoded_password_hash")
                        .build();
            }
            throw new UsernameNotFoundException("User not found");
        };
    }

    /**
     * Configure the authentication provider for testing
     */
    @Bean
    @Primary
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    @Primary
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    // Authentication endpoints
                    "/api/v1/auth/register", 
                    "/api/v1/auth/authenticate", 
                    "/api/v1/auth/setup-admin",
                    // Swagger UI and API Docs
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    // H2 Console (if used)
                    "/h2-console/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        // Enable H2 Console Frame Options for Dev Environment
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}