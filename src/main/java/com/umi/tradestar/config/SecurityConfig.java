package com.umi.tradestar.config;

import com.umi.tradestar.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security configuration class that integrates Keycloak with Spring Security.
 * Configures JWT-based authentication and role-based authorization.
 * This configuration is only active in non-test profiles and when no other SecurityFilterChain is present.
 *
 * @author VrushankPatel
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Profile("!test")
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final KeycloakAuthenticationProvider keycloakAuthenticationProvider;
    private final KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter;
    private final KeycloakPreAuthActionsFilter keycloakPreAuthActionsFilter;
    private final AuthenticationProvider jwtAuthenticationProvider;

    /**
     * Configure the authentication providers
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Configure Keycloak authentication provider
        auth.authenticationProvider(keycloakAuthenticationProvider);
        
        // Configure local JWT authentication provider
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    // Authentication endpoints
                    "/api/v1/auth/register", 
                    "/api/v1/auth/authenticate", 
                    "/api/v1/auth/setup-admin", 
                    // Keycloak-specific endpoints
                    "/sso/**",
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
            )
            .addFilterBefore(keycloakPreAuthActionsFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(keycloakAuthenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Enable H2 Console Frame Options for Dev Environment
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}