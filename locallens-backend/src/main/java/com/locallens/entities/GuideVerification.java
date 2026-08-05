package com.locallens.entities;

import java.time.LocalDateTime;

import com.locallens.enums.VerificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "guide_verifications")
public class GuideVerification extends BaseEntity {

    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "guide_id",
        nullable = false,
        unique = true
    )
    private User guide;

    @Column(
        name = "document_url",
        nullable = false,
        length = 500
    )
    private String documentUrl;

    @Column(
        name = "document_type",
        nullable = false,
        length = 50
    )
    private String documentType;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false,
        length = 20
    )
    private VerificationStatus status =
            VerificationStatus.PENDING;

    @Column(
        name = "rejection_reason",
        length = 500
    )
    private String rejectionReason;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    /*
     * Many guide verification records may be handled
     * by one administrator.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_admin_id")
    private User verifiedBy;

    public void approve(User admin) {
        this.status = VerificationStatus.VERIFIED;
        this.verifiedBy = admin;
        this.verifiedAt = LocalDateTime.now();
        this.rejectionReason = null;
    }

    public void reject(User admin, String reason) {
        this.status = VerificationStatus.REJECTED;
        this.verifiedBy = admin;
        this.verifiedAt = LocalDateTime.now();
        this.rejectionReason = reason;
    }
}