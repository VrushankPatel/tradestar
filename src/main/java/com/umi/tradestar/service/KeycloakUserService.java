package com.umi.tradestar.service;

import com.umi.tradestar.model.User;
import com.umi.tradestar.model.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service for managing users in Keycloak.
 * Provides methods for creating, updating, and retrieving user information from Keycloak.
 *
 * @author VrushankPatel
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserService {
    
    private static final Logger log = LoggerFactory.getLogger(KeycloakUserService.class);

    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

    /**
     * Creates a new user in Keycloak.
     *
     * @param user the user entity to create in Keycloak
     * @param password the user's password
     * @return the Keycloak user ID if successful, null otherwise
     */
    public String createUser(User user, String password) {
        try {
            // Create user representation
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setEnabled(user.isEnabled());
            userRepresentation.setUsername(user.getEmail());
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            userRepresentation.setEmailVerified(true);
            
            // Add additional attributes if needed
            Map<String, List<String>> attributes = Map.of(
                "app_user_id", Collections.singletonList(user.getId().toString())
            );
            userRepresentation.setAttributes(attributes);

            // Get realm
            RealmResource realmResource = keycloakAdmin.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Create user (requires admin role)
            Response response = usersResource.create(userRepresentation);
            if (response.getStatus() == 201) {
                String userId = getCreatedUserId(response);
                
                // Set password credential
                setUserPassword(userId, password);
                
                // Assign role
                assignUserRole(userId, user.getRole());
                
                log.info("User created in Keycloak: {}", user.getEmail());
                return userId;
            } else {
                log.error("Failed to create user in Keycloak. Status: {}", response.getStatus());
                return null;
            }
        } catch (Exception e) {
            log.error("Error creating user in Keycloak", e);
            return null;
        }
    }

    /**
     * Updates an existing user in Keycloak.
     *
     * @param user the user entity with updated information
     * @return true if the update was successful, false otherwise
     */
    public boolean updateUser(User user) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(user.getEmail());
            
            if (users.isEmpty()) {
                log.error("User not found in Keycloak: {}", user.getEmail());
                return false;
            }
            
            UserRepresentation userRepresentation = users.get(0);
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            userRepresentation.setEnabled(user.isEnabled());
            
            // Update user
            UserResource userResource = realmResource.users().get(userRepresentation.getId());
            userResource.update(userRepresentation);
            
            // Update role if needed
            updateUserRole(userRepresentation.getId(), user.getRole());
            
            log.info("User updated in Keycloak: {}", user.getEmail());
            return true;
        } catch (Exception e) {
            log.error("Error updating user in Keycloak", e);
            return false;
        }
    }

    /**
     * Updates a user's password in Keycloak.
     *
     * @param email the user's email
     * @param newPassword the new password
     * @return true if the password was updated, false otherwise
     */
    public boolean updatePassword(String email, String newPassword) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(email);
            
            if (users.isEmpty()) {
                return false;
            }
            
            String userId = users.get(0).getId();
            return setUserPassword(userId, newPassword);
        } catch (Exception e) {
            log.error("Error updating password in Keycloak", e);
            return false;
        }
    }

    /**
     * Deletes a user from Keycloak.
     *
     * @param email the email of the user to delete
     * @return true if the user was deleted, false otherwise
     */
    public boolean deleteUser(String email) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(email);
            
            if (users.isEmpty()) {
                return false;
            }
            
            String userId = users.get(0).getId();
            realmResource.users().get(userId).remove();
            log.info("User deleted from Keycloak: {}", email);
            return true;
        } catch (Exception e) {
            log.error("Error deleting user from Keycloak", e);
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
     * @return true if the password was set, false otherwise
     */
    private boolean setUserPassword(String userId, String password) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Create password credential
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);

            // Set password credential
            usersResource.get(userId).resetPassword(credential);
            return true;
        } catch (Exception e) {
            log.error("Error setting password in Keycloak", e);
            return false;
        }
    }

    /**
     * Assigns a role to a user.
     *
     * @param userId the user ID
     * @param role the role to assign
     */
    private void assignUserRole(String userId, Role role) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Get role representation
            String roleName = role != null ? role.name() : Role.TRADER.name();
            RoleRepresentation roleRepresentation = realmResource.roles().get(roleName).toRepresentation();

            // Assign realm role to user
            usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
        } catch (Exception e) {
            log.error("Error assigning role in Keycloak", e);
        }
    }

    /**
     * Updates a user's role in Keycloak.
     *
     * @param userId the user ID
     * @param newRole the new role to assign
     */
    private void updateUserRole(String userId, Role newRole) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);
            UserResource userResource = realmResource.users().get(userId);
            
            // Remove all current realm roles
            List<RoleRepresentation> currentRoles = userResource.roles().realmLevel().listAll();
            if (!currentRoles.isEmpty()) {
                userResource.roles().realmLevel().remove(currentRoles);
            }
            
            // Assign new role
            assignUserRole(userId, newRole);
        } catch (Exception e) {
            log.error("Error updating role in Keycloak", e);
        }
    }

    /**
     * Finds a user in Keycloak by email.
     *
     * @param email the user's email
     * @return the user representation if found, null otherwise
     */
    public UserRepresentation findUserByEmail(String email) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(email);
            
            return users.isEmpty() ? null : users.get(0);
        } catch (Exception e) {
            log.error("Error finding user in Keycloak", e);
            return null;
        }
    }
}