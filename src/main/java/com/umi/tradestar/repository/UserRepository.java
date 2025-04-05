package com.umi.tradestar.repository;

import com.umi.tradestar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Provides methods for user-related database operations.
 *
 * @author VrushankPatel
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists with the given email address.
     *
     * @param email the email address to check
     * @return true if a user exists with the email, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Update the enabled status of a user.
     *
     * @param email the email of the user to update
     * @param enabled the new enabled status
     * @return the number of users updated
     */
    @Modifying
    @Query("UPDATE User u SET u.enabled = :enabled WHERE u.email = :email")
    int updateEnabledStatus(@Param("email") String email, @Param("enabled") boolean enabled);
}