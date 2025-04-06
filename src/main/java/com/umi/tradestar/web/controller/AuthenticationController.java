package com.umi.tradestar.web.controller;

import com.umi.tradestar.model.enums.Role;
import com.umi.tradestar.service.AuthenticationService;
import com.umi.tradestar.web.dto.AuthenticationRequest;
import com.umi.tradestar.web.dto.AuthenticationResponse;
import com.umi.tradestar.web.dto.RegisterRequest;
import com.umi.tradestar.web.dto.RegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related requests.
 * Provides endpoints for user registration and authentication.
 *
 * @author VrushankPatel
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Endpoint for user registration.
     *
     * @param request the registration request
     * @return RegisterResponse containing registration status
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     * Special endpoint for initial admin setup.
     * This endpoint should be disabled in production after initial setup.
     *
     * @param request the registration request
     * @return RegisterResponse containing registration status
     */
    @PostMapping("/setup-admin")
    @Operation(summary = "Initial admin setup (development only)")
    public ResponseEntity<RegisterResponse> setupAdmin(@RequestBody RegisterRequest request) {
        // Force the role to be ADMIN
        request.setRole(Role.ADMIN);
        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     * Endpoint for user authentication.
     *
     * @param request the authentication request
     * @return AuthenticationResponse containing the JWT token
     */
    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate a user")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    
    /**
     * Endpoint to enable a user account.
     * Requires ADMIN role.
     *
     * @param email the email of the user to enable
     * @return ResponseEntity with success or failure message
     */
    @PostMapping("/enable/{email}")
    @Operation(summary = "Enable a user account", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> enableUser(@PathVariable String email) {
        boolean enabled = authenticationService.enableUser(email);
        if (enabled) {
            return ResponseEntity.ok("User account enabled successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Endpoint to disable a user account.
     * Requires ADMIN role.
     *
     * @param email the email of the user to disable
     * @return ResponseEntity with success or failure message
     */
    @PostMapping("/disable/{email}")
    @Operation(summary = "Disable a user account", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> disableUser(@PathVariable String email) {
        boolean disabled = authenticationService.disableUser(email);
        if (disabled) {
            return ResponseEntity.ok("User account disabled successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}