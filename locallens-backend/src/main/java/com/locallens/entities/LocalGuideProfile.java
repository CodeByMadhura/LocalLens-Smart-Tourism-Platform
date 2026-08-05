package com.locallens.entities;

import com.locallens.enums.VerificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "local_guide_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalGuideProfile extends BaseEntity {

    /*
     * Every local guide profile belongs to exactly one user.
     *
     * The user_id column is unique because one user can have
     * only one local guide profile.
     */
    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true
    )
    private User user;

    /*
     * Professional introduction shown to travellers.
     */
    @Column(
        name = "about_me",
        columnDefinition = "TEXT"
    )
    private String aboutMe;

    /*
     * Number of years the guide has worked as a local guide.
     */
    @Column(name = "experience_years")
    private Integer experienceYears;

    /*
     * Current profession or occupation of the guide.
     */
    @Column(
        name = "occupation",
        length = 100
    )
    private String occupation;

    /*
     * Examples:
     * History, Food Tours, Trekking, Wildlife, Photography.
     */
    @Column(
        name = "expertise",
        length = 500
    )
    private String expertise;

    /*
     * Examples:
     * Marathi, Hindi, English.
     *
     * For now, these languages are stored as comma-separated text.
     */
    @Column(
        name = "languages_spoken",
        length = 500
    )
    private String languagesSpoken;

    /*
     * Guide's personal website or portfolio.
     */
    @Column(
        name = "website_url",
        length = 500
    )
    private String websiteUrl;

    /*
     * URL or stored path of the guide verification document.
     */
    @Column(
        name = "verification_document_url",
        length = 500
    )
    private String verificationDocumentUrl;

    /*
     * Admin verification status of the guide.
     */
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(
        name = "verification_status",
        nullable = false,
        length = 30
    )
    private VerificationStatus verificationStatus =
            VerificationStatus.PENDING;

    /*
     * Indicates whether all required guide-profile details
     * have been submitted.
     */
    @Builder.Default
    @Column(
        name = "profile_completed",
        nullable = false
    )
    private boolean profileCompleted = false;
}

