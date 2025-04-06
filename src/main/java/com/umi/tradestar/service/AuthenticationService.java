package com.umi.tradestar.service;

import com.umi.tradestar.model.User;
import com.umi.tradestar.model.enums.Role;
import com.umi.tradestar.repository.UserRepository;
import com.umi.tradestar.security.JwtService;
import com.umi.tradestar.web.dto.AuthenticationRequest;
import com.umi.tradestar.web.dto.AuthenticationResponse;
import com.umi.tradestar.web.dto.RegisterRequest;
import com.umi.tradestar.web.dto.RegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class handling user authentication operations including registration and login.
 * Integrates with Keycloak for identity management.
 *
 * @author VrushankPatel
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final KeycloakService keycloakService;
    private final KeycloakUserService keycloakUserService;

    /**
     * Registers a new user in the system and in Keycloak.
     *
     * @param request the registration request containing user details
     * @return RegisterResponse containing registration status
     */
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create user in local database first
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.TRADER)
                .enabled(true) // Ensure the user is enabled by default
                .build();

        userRepository.save(user);
        
        // Then create user in Keycloak using the new KeycloakUserService
        String keycloakUserId = keycloakUserService.createUser(user, request.getPassword());
        if (keycloakUserId == null) {
            // Rollback the database save if Keycloak creation fails
            userRepository.delete(user);
            throw new RuntimeException("Failed to create user in Keycloak");
        }
        
        log.info("User registered successfully in both Keycloak and local database: {}", user.getEmail());
        
        return RegisterResponse.builder()
                .message("User registered successfully")
                .email(user.getEmail())
                .build();
    }

    /**
     * Authenticates a user through Keycloak and generates a JWT token.
     * The authentication is delegated to Spring Security which is configured to use Keycloak.
     *
     * @param request the authentication request containing credentials
     * @return AuthenticationResponse containing the JWT token
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // First verify the user exists in Keycloak
            if (keycloakUserService.findUserByEmail(request.getEmail()) == null) {
                throw new com.umi.tradestar.exception.AuthenticationException(
                    com.umi.tradestar.exception.AuthenticationException.ERROR_CODE_USER_NOT_FOUND, 
                    "User not found in Keycloak");
            }
            
            // Authentication is handled by Spring Security with Keycloak adapter
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            
            // After Keycloak authentication succeeds, get the user from our database
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new com.umi.tradestar.exception.AuthenticationException(
                        com.umi.tradestar.exception.AuthenticationException.ERROR_CODE_USER_NOT_FOUND, 
                        "User not found in local database"));
            
            // Check if the user account is enabled
            if (!user.isEnabled()) {
                throw com.umi.tradestar.exception.AuthenticationException.userDisabled();
            }
            
            // Generate JWT token for the authenticated user
            var jwtToken = jwtService.generateToken(user);
            log.info("User authenticated successfully: {}", user.getEmail());
            
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            log.error("Authentication failed: Invalid credentials for user {}", request.getEmail());
            // Wrap Spring Security's BadCredentialsException in our custom AuthenticationException
            throw com.umi.tradestar.exception.AuthenticationException.invalidCredentials();
        }
    }
    
    /**
     * Enables a user account in both Keycloak and local database.
     *
     * @param email the email of the user to enable
     * @return true if the user was enabled, false if the user was not found
     */
    @Transactional
    public boolean enableUser(String email) {
        // Get user from database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
        
        // Enable user in local database
        user.setEnabled(true);
        userRepository.save(user);
        
        // Enable user in Keycloak using the new KeycloakUserService
        boolean keycloakUserUpdated = keycloakUserService.updateUser(user);
        
        if (!keycloakUserUpdated) {
            log.warn("Failed to enable user in Keycloak: {}", email);
        }
        
        log.info("User enabled: {}", email);
        return true;
    }
    
    /**
     * Disables a user account in both Keycloak and local database.
     *
     * @param email the email of the user to disable
     * @return true if the user was disabled, false if the user was not found
     */
    @Transactional
    public boolean disableUser(String email) {
        // Get user from database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
        
        // Disable user in local database
        user.setEnabled(false);
        userRepository.save(user);
        
        // Disable user in Keycloak using the new KeycloakUserService
        boolean keycloakUserUpdated = keycloakUserService.updateUser(user);
        
        if (!keycloakUserUpdated) {
            log.warn("Failed to disable user in Keycloak: {}", email);
        }
        
        log.info("User disabled: {}", email);
        return true;
    }
}