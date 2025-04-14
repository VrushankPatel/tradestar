package com.umi.tradestar.config;

import com.umi.tradestar.model.User;
import com.umi.tradestar.model.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class TestSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        User testUser = User.builder()
                .id("1")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .role(Role.TRADER)
                .build();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(testUser.getEmail())
                .password(passwordEncoder().encode("password"))
                .authorities(testUser.getRole().name())
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}