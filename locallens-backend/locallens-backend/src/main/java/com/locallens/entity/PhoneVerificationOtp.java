package com.locallens.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phone_verification_otps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PhoneVerificationOtp extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "otp_hash", nullable = false, length = 255)
    private String otpHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;


    @Column(nullable = false)
    private boolean used = false;


    @Column(name = "attempt_count", nullable = false)
    private int attemptCount = 0;
}
