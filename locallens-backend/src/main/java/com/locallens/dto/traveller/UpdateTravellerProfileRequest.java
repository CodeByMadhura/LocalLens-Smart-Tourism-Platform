package com.locallens.dto.traveller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.locallens.enums.GenderType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UpdateTravellerProfileRequest {

    /*
     * ---------------------------------------------------------
     * User information
     * ---------------------------------------------------------
     */

    @Size(
        max = 50,
        message = "First name cannot exceed 50 characters"
    )
    private String firstName;

    @Size(
        max = 100,
        message = "Last name cannot exceed 100 characters"
    )
    private String lastName;

    /*
     * Allows:
     * 9876543210
     * +919876543210
     * +91 98765 43210
     * 98765-43210
     */
    @Pattern(
        regexp = "^$|^[0-9+()\\-\\s]{7,20}$",
        message = "Please enter a valid phone number"
    )
    private String phoneNumber;

    private GenderType gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    /*
     * ---------------------------------------------------------
     * Traveller profile information
     * ---------------------------------------------------------
     */

    @Size(
        max = 150,
        message = "Occupation cannot exceed 150 characters"
    )
    private String occupation;

    /*
     * Frontend sends aboutMe.
     *
     * This value should be stored in TravellerProfile.bio.
     */
    @Size(
        max = 2000,
        message = "About me cannot exceed 2000 characters"
    )
    private String aboutMe;

    @Builder.Default
    private List<String> languagesSpoken =
            new ArrayList<>();

    /*
     * ---------------------------------------------------------
     * Address information
     * ---------------------------------------------------------
     */

    private Boolean sameAddress;

    @Valid
    private TravellerAddressRequest permanentAddress;

    @Valid
    private TravellerAddressRequest currentAddress;

    /*
     * ---------------------------------------------------------
     * Travel preferences
     * ---------------------------------------------------------
     */

    @Valid
    private TravellerPreferenceRequest travelPreferences;

    /*
     * ---------------------------------------------------------
     * Optional fields
     * ---------------------------------------------------------
     */

    @Size(
        max = 150,
        message = "Emergency contact name cannot exceed 150 characters"
    )
    private String emergencyContactName;

    @Pattern(
        regexp = "^$|^[0-9+()\\-\\s]{7,20}$",
        message = "Please enter a valid emergency contact number"
    )
    private String emergencyContactNumber;

    @Size(
        max = 500,
        message = "Profile image URL cannot exceed 500 characters"
    )
    private String profileImageUrl;

    @Builder.Default
    private List<String> accessibilityPreferences =
            new ArrayList<>();

    /*
     * ---------------------------------------------------------
     * Backward-compatible fields
     * ---------------------------------------------------------
     *
     * These fields can be temporarily retained if another old
     * frontend component or API request still uses them.
     *
     * The new TravellerProfile page should use:
     *
     * aboutMe
     * permanentAddress
     * currentAddress
     * travelPreferences
     */

    @Deprecated
    private String bio;

    @Deprecated
    private TravellerAddressRequest address;

    @Deprecated
    private String travelStyle;

    @Deprecated
    private String preferredLanguage;

    @Deprecated
    private String preferredTravelType;

    @Deprecated
    private String homeCity;

    @Deprecated
    private String homeState;

    @Deprecated
    private String country;

    @Builder.Default
    @Deprecated
    private List<String> interests = new ArrayList<>();
}