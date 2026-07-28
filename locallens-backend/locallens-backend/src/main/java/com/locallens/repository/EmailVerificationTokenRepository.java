package com.locallens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.locallens.entity.EmailVerificationToken;
import com.locallens.entity.User;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    EmailVerificationToken findByToken(String token);

    EmailVerificationToken findByTokenAndUsedFalse(
            String token
    );

    List<EmailVerificationToken> findByUserAndUsedFalse(
            User user
    );

    void deleteByUser(User user);
}