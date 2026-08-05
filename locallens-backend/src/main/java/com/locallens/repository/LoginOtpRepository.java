package com.locallens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.locallens.entities.LoginOtp;
import com.locallens.entities.User;
import com.locallens.enums.OtpChannel;
import com.locallens.enums.OtpPurpose;

@Repository
public interface LoginOtpRepository
        extends JpaRepository<LoginOtp, Long> {

    /*
     * Finds the latest unused OTP that matches the user,
     * OTP value, channel and purpose.
     *
     * Used while verifying a login OTP.
     */
    Optional<LoginOtp>
            findTopByUserAndOtpAndChannelAndPurposeAndUsedFalseOrderByCreatedAtDesc(
                    User user,
                    String otp,
                    OtpChannel channel,
                    OtpPurpose purpose
            );

    /*
     * Finds the latest unused OTP without checking the OTP value.
     *
     * Useful for checking whether the user already has an
     * active login OTP.
     */
    Optional<LoginOtp>
            findTopByUserAndChannelAndPurposeAndUsedFalseOrderByCreatedAtDesc(
                    User user,
                    OtpChannel channel,
                    OtpPurpose purpose
            );

    /*
     * Finds all unused OTPs so old OTPs can be invalidated
     * before generating a new one.
     */
    List<LoginOtp> findByUserAndPurposeAndUsedFalse(
            User user,
            OtpPurpose purpose
    );
}