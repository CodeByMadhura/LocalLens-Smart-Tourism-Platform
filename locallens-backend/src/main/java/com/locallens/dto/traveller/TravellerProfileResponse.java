package com.locallens.dto.traveller;

import java.math.BigDecimal;
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
     * User information.
     */

    private Long userId;

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
     * TravellerProfile information.
     */

    private Long travellerProfileId;

    private String bio;

    private String occupation;

    private String travelStyle;

    private String preferredLanguage;

    private String homeCity;

    private String homeState;

    private String country;

    private BigDecimal preferredBudget;

    private String preferredTravelType;

    private String emergencyContactName;

    private String emergencyContactNumber;

    private String profileImageUrl;

    @Builder.Default
    private List<String> interests = new ArrayList<>();

    @Builder.Default
    private List<String> languagesSpoken = new ArrayList<>();

    @Builder.Default
    private List<String> accessibilityPreferences =
            new ArrayList<>();

    private TravellerAddressResponse address;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}