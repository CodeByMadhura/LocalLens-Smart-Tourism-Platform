package com.locallens.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.locallens.entities.ForgotPasswordOtp;
import com.locallens.entities.User;

public interface ForgotPasswordOtpRepository
        extends JpaRepository<ForgotPasswordOtp, Long> {

    Optional<ForgotPasswordOtp>
            findTopByUserAndUsedFalseOrderByCreatedAtDesc(
                    User user
            );

    Optional<ForgotPasswordOtp>
            findTopByUserAndOtpAndUsedFalseOrderByCreatedAtDesc(
                    User user,
                    String otp
            );

    void deleteByUser(User user);
}