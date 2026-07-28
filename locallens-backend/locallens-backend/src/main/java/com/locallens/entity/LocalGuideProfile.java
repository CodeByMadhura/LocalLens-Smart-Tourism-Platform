package com.locallens.entity;

import com.locallens.enums.VerificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	name = "local_guide_profiles",
	uniqueConstraints = {
			@UniqueConstraint(
					name = "uk_guide_profile_user",
					columnNames = "user_id"
					)
			
}
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalGuideProfile extends BaseEntity{
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "about_me", columnDefinition = "TEXT")
	private String aboutMe;
	
	@Column(name = "experience_years")
	private Integer experienceYears;
	
	@Column(length = 100)
	private String occupation;
	
	@Column(name = "verification_document_url",
			length = 500)
	private String verificationDocumentUrl;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default
	@Column(name = "verification_status", nullable = false)
	private VerificationStatus verfificationStatus = VerificationStatus.PENDING;
	
	@Builder.Default
	@Column(name = "profile_completed", nullable = false)
	private boolean profileCompleted = false;
	
	

}
