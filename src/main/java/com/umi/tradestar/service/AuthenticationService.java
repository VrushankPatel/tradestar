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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class handling user authentication operations including registration and login.
 *
 * @author VrushankPatel
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user in the system.
     *
     * @param request the registration request containing user details
     * @return AuthenticationResponse containing the JWT token
     */
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.TRADER)
                .enabled(true) // Ensure the user is enabled by default
                .build();

        userRepository.save(user);
        return RegisterResponse.builder()
                .message("User registered successfully")
                .email(user.getEmail())
                .build();
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request the authentication request containing credentials
     * @return AuthenticationResponse containing the JWT token
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new com.umi.tradestar.exception.AuthenticationException(
                        com.umi.tradestar.exception.AuthenticationException.ERROR_CODE_USER_NOT_FOUND, 
                        "User not found"));
            
            // Check if the user account is enabled
            if (!user.isEnabled()) {
                throw com.umi.tradestar.exception.AuthenticationException.userDisabled();
            }
            
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            // Wrap Spring Security's BadCredentialsException in our custom AuthenticationException
            throw com.umi.tradestar.exception.AuthenticationException.invalidCredentials();
        }
    }
    
    /**
     * Enables a user account.
     *
     * @param email the email of the user to enable
     * @return true if the user was enabled, false if the user was not found
     */
    @Transactional
    public boolean enableUser(String email) {
        int updated = userRepository.updateEnabledStatus(email, true);
        return updated > 0;
    }
    
    /**
     * Disables a user account.
     *
     * @param email the email of the user to disable
     * @return true if the user was disabled, false if the user was not found
     */
    @Transactional
    public boolean disableUser(String email) {
        int updated = userRepository.updateEnabledStatus(email, false);
        return updated > 0;
    }
}