package com.locallens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.locallens.entities.EmailVerificationToken;
import com.locallens.entities.User;

@Repository
public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long> {

    /*
     * Finds the latest unused registration OTP for the
     * specified user and OTP value.
     *
     * This method is used while verifying the OTP after registration.
     */
    Optional<EmailVerificationToken>
            findTopByUserAndTokenAndUsedFalseOrderByCreatedAtDesc(
                    User user,
                    String token
            );

    /*
     * Finds the latest unused registration OTP for a user.
     *
     * This can be useful when checking whether an active OTP
     * already exists before generating another OTP.
     */
    Optional<EmailVerificationToken>
            findTopByUserAndUsedFalseOrderByCreatedAtDesc(
                    User user
            );

    /*
     * Finds all unused registration OTPs for a user.
     *
     * Before sending a new OTP, old unused OTPs can be marked
     * as used so that only the latest OTP remains valid.
     */
    List<EmailVerificationToken> findByUserAndUsedFalse(
            User user
    );

    /*
     * Deletes all registration OTP records belonging to a user.
     *
     * This is mainly useful while deleting users or cleaning
     * test data.
     */
    void deleteByUser(User user);
}