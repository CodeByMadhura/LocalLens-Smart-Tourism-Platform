package com.locallens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.locallens.entity.PhoneVerificationOtp;
import com.locallens.entity.User;

public interface PhoneVerificationOtpRepository
        extends JpaRepository<PhoneVerificationOtp, Long> {

    List<PhoneVerificationOtp> findByUserAndUsedFalse(User user);

    PhoneVerificationOtp
        findTopByUserAndUsedFalseOrderByCreatedAtDesc(User user);

    void deleteByUser(User user);
}