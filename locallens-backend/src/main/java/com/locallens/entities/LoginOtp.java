package com.locallens.entities;

import java.time.LocalDateTime;

import com.locallens.enums.OtpChannel;
import com.locallens.enums.OtpPurpose;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "login_otps",
    indexes = {
        @Index(
            name = "idx_login_otp_user",
            columnList = "user_id"
        ),
        @Index(
            name = "idx_login_otp_value",
            columnList = "otp"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class LoginOtp extends BaseEntity {

    @Column(
        name = "otp",
        nullable = false,
        length = 6
    )
    private String otp;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "channel",
        nullable = false,
        length = 20
    )
    private OtpChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "purpose",
        nullable = false,
        length = 30
    )
    private OtpPurpose purpose;

    @Column(
        name = "expires_at",
        nullable = false
    )
    private LocalDateTime expiresAt;

    @Column(
        name = "used",
        nullable = false
    )
    private boolean used = false;

    @Column(
        name = "attempt_count",
        nullable = false
    )
    private int attemptCount = 0;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;

    public LoginOtp(
            User user,
            String otp,
            OtpChannel channel,
            OtpPurpose purpose,
            LocalDateTime expiresAt) {

        this.user = user;
        this.otp = otp;
        this.channel = channel;
        this.purpose = purpose;
        this.expiresAt = expiresAt;
        this.used = false;
        this.attemptCount = 0;
    }

    public boolean isExpired() {
        return expiresAt == null ||
                LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isValid() {
        return !used && !isExpired();
    }

    public void markAsUsed() {
        this.used = true;
    }

    public void incrementAttemptCount() {
        this.attemptCount++;
    }
}