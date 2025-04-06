package com.umi.tradestar.service;

import com.umi.tradestar.model.User;
import com.umi.tradestar.model.enums.Role;
import com.umi.tradestar.web.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

/**
 * Service for managing Keycloak users and authentication.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {
    
    private static final Logger log = LoggerFactory.getLogger(KeycloakService.class);

    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

    /**
     * Creates a new user in Keycloak.
     *
     * @param request the registration request
     * @return true if user was created successfully, false otherwise
     */
    public boolean createUser(RegisterRequest request) {
        try {
            // Create user representation
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(request.getEmail());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmailVerified(true);

            // Get realm
            RealmResource realmResource = keycloakAdmin.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Create user (requires admin role)
            Response response = usersResource.create(user);
            if (response.getStatus() == 201) {
                String userId = getCreatedUserId(response);
                
                // Set password credential
                setUserPassword(userId, request.getPassword());
                
                // Assign role
                assignUserRole(userId, request.getRole());
                
                return true;
            } else {
                log.error("Failed to create user in Keycloak. Status: {}", response.getStatus());
                return false;
            }
        } catch (Exception e) {
            log.error("Error creating user in Keycloak", e);
            return false;
        }
    }

    /**
     * Extracts the user ID from the response.
     *
     * @param response the response from creating a user
     * @return the user ID
     */
    private String getCreatedUserId(Response response) {
        String locationHeader = response.getHeaderString("Location");
        return locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
    }

    /**
     * Sets the password for a user.
     *
     * @param userId the user ID
     * @param password the password to set
     */
    private void setUserPassword(String userId, String password) {
        RealmResource realmResource = keycloakAdmin.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Create password credential
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        // Set password credential
        usersResource.get(userId).resetPassword(credential);
    }

    /**
     * Assigns a role to a user.
     *
     * @param userId the user ID
     * @param role the role to assign
     */
    private void assignUserRole(String userId, Role role) {
        RealmResource realmResource = keycloakAdmin.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Get role representation
        String roleName = role != null ? role.name() : Role.TRADER.name();
        RoleRepresentation roleRepresentation = realmResource.roles().get(roleName).toRepresentation();

        // Assign realm role to user
        usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    /**
     * Enables a user in Keycloak.
     *
     * @param email the email of the user to enable
     * @return true if the user was enabled, false otherwise
     */
    public boolean enableUser(String email) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(email);
            
            if (users.isEmpty()) {
                return false;
            }
            
            UserRepresentation user = users.get(0);
            user.setEnabled(true);
            
            realmResource.users().get(user.getId()).update(user);
            return true;
        } catch (Exception e) {
            log.error("Error enabling user in Keycloak", e);
            return false;
        }
    }

    /**
     * Disables a user in Keycloak.
     *
     * @param email the email of the user to disable
     * @return true if the user was disabled, false otherwise
     */
    public boolean disableUser(String email) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(email);
            
            if (users.isEmpty()) {
                return false;
            }
            
            UserRepresentation user = users.get(0);
            user.setEnabled(false);
            
            realmResource.users().get(user.getId()).update(user);
            return true;
        } catch (Exception e) {
            log.error("Error disabling user in Keycloak", e);
            return false;
        }
    }
}