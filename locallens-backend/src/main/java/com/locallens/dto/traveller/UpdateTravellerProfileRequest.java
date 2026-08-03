package com.locallens.dto.traveller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.locallens.enums.GenderType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
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
     * Fields belonging to the User entity.
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

    @Pattern(
        regexp = "^[0-9]{10,15}$",
        message = "Phone number must contain 10 to 15 digits"
    )
    private String phoneNumber;

    private GenderType gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    /*
     * Fields belonging to TravellerProfile.
     */

    @Size(
        max = 2000,
        message = "Bio cannot exceed 2000 characters"
    )
    private String bio;

    @Size(
        max = 150,
        message = "Occupation cannot exceed 150 characters"
    )
    private String occupation;

    @Size(
        max = 100,
        message = "Travel style cannot exceed 100 characters"
    )
    private String travelStyle;

    @Size(
        max = 100,
        message = "Preferred language cannot exceed 100 characters"
    )
    private String preferredLanguage;

    @Size(
        max = 100,
        message = "Home city cannot exceed 100 characters"
    )
    private String homeCity;

    @Size(
        max = 100,
        message = "Home state cannot exceed 100 characters"
    )
    private String homeState;

    @Size(
        max = 100,
        message = "Country cannot exceed 100 characters"
    )
    private String country;

    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Preferred budget cannot be negative"
    )
    private BigDecimal preferredBudget;

    @Size(
        max = 50,
        message = "Preferred travel type cannot exceed 50 characters"
    )
    private String preferredTravelType;

    @Size(
        max = 150,
        message = "Emergency contact name cannot exceed 150 characters"
    )
    private String emergencyContactName;

    @Pattern(
        regexp = "^$|^[0-9]{10,15}$",
        message = "Emergency contact number must contain 10 to 15 digits"
    )
    private String emergencyContactNumber;

    @Size(
        max = 500,
        message = "Profile image URL cannot exceed 500 characters"
    )
    private String profileImageUrl;

    @Builder.Default
    private List<String> interests = new ArrayList<>();

    @Builder.Default
    private List<String> languagesSpoken = new ArrayList<>();

    @Builder.Default
    private List<String> accessibilityPreferences =
            new ArrayList<>();

    @Valid
    private TravellerAddressRequest address;
}