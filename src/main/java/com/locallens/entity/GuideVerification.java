package com.locallens.entity;

import java.time.LocalDateTime;

import com.locallens.enums.VerificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "guide_verifications")
public class GuideVerification extends BaseEntity{
	
	@OneToOne(optional = false)
	@JoinColumn(name = "guide_id, nullable = false, unique = true")
	private User guide;
	
	@Column(name = "documnet_url", nullable = false)
	private String documentUrl;
	
	@Column(name = "document_type", length = 50)
	private String documentType;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private VerificationStatus status = VerificationStatus.PENDING;
	
	@Column(name = "rejection_reason")
	private String rejectionReason;
	
	@Column(name = "verified_at")
	private LocalDateTime verifiedAt;
	
	@OneToOne
	@JoinColumn(name = "verified_by_admin_id")
	private User verifiedBy;

}
