package com.locallens.dto.traveller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.locallens.enums.GenderType;
import com.locallens.enums.UserRole;
import com.locallens.enums.VerificationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravellerProfileResponse {

    /*
     * ---------------------------------------------------------
     * User information
     * ---------------------------------------------------------
     */

    private Long userId;

    private Long travellerProfileId;

    private String firstName;

    private String lastName;

    private String fullName;

    private String email;

    private String phoneNumber;

    private GenderType gender;

    private LocalDate dateOfBirth;

    private UserRole role;

    private boolean active;

    private boolean emailVerified;

    private boolean phoneVerified;

    private boolean profileCompleted;

    private VerificationStatus verificationStatus;

    /*
     * ---------------------------------------------------------
     * Traveller profile information
     * ---------------------------------------------------------
     */

    /*
     * Database field may be named bio, but frontend reads
     * aboutMe first and also supports bio as a fallback.
     */
    private String aboutMe;

    private String bio;

    private String occupation;

    private String profileImageUrl;

    @Builder.Default
    private List<String> languagesSpoken =
            new ArrayList<>();

    @Builder.Default
    private List<String> accessibilityPreferences =
            new ArrayList<>();

    /*
     * ---------------------------------------------------------
     * Address information
     * ---------------------------------------------------------
     */

    private Boolean sameAddress;

    private TravellerAddressResponse permanentAddress;

    private TravellerAddressResponse currentAddress;

    /*
     * Legacy address field.
     *
     * It can contain the current address for compatibility with
     * older frontend files.
     */
    private TravellerAddressResponse address;

    /*
     * ---------------------------------------------------------
     * Travel preferences
     * ---------------------------------------------------------
     */

    private TravellerPreferenceRequest travelPreferences;

    /*
     * ---------------------------------------------------------
     * Legacy profile fields
     * ---------------------------------------------------------
     *
     * These can remain temporarily if other dashboard files use
     * the older flat response structure.
     */

    private String travelStyle;

    private String preferredLanguage;

    private String homeCity;

    private String homeState;

    private String country;

    private java.math.BigDecimal preferredBudget;

    private String preferredTravelType;

    private String emergencyContactName;

    private String emergencyContactNumber;

    @Builder.Default
    private List<String> interests =
            new ArrayList<>();

    /*
     * ---------------------------------------------------------
     * Audit information
     * ---------------------------------------------------------
     */

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}