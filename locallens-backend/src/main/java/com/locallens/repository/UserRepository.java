package com.locallens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.locallens.entities.User;
import com.locallens.enums.UserRole;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    // =========================================================
    // AUTHENTICATION AND REGISTRATION QUERIES
    // =========================================================

    /**
     * Finds a user by email without considering letter case.
     */
    Optional<User> findByEmailIgnoreCase(
            String email
    );

    /**
     * Finds a user by phone number.
     */
    Optional<User> findByPhoneNumber(
            String phoneNumber
    );

    /**
     * Checks whether an email address is already registered.
     */
    boolean existsByEmailIgnoreCase(
            String email
    );

    /**
     * Checks whether a phone number is already registered.
     */
    boolean existsByPhoneNumber(
            String phoneNumber
    );

    // =========================================================
    // ADMIN USER-MANAGEMENT QUERIES
    // =========================================================

    /**
     * Returns registered users belonging to a particular role.
     *
     * Used for:
     * TRAVELLER
     * LOCAL_GUIDE
     */
    List<User> findByRoleOrderByIdDesc(
            UserRole role
    );

    /**
     * Returns users belonging to a particular role
     * and having the provided active status.
     */
    List<User> findByRoleAndActiveOrderByIdDesc(
            UserRole role,
            boolean active
    );

    /**
     * Counts users belonging to a particular role.
     *
     * Used for admin dashboard summary cards.
     */
    long countByRole(
            UserRole role
    );

    /**
     * Counts active users.
     *
     * This represents active accounts, not users who are
     * currently online.
     */
    long countByActiveTrue();

    /**
     * Counts inactive or disabled user accounts.
     */
    long countByActiveFalse();

    /**
     * Counts active accounts belonging to a specific role.
     */
    long countByRoleAndActiveTrue(
            UserRole role
    );

    /**
     * Returns the five most recently registered users
     * based on the generated user ID.
     *
     * Use this method when the User entity does not have
     * a createdAt field.
     */
    List<User> findTop5ByOrderByIdDesc();
}