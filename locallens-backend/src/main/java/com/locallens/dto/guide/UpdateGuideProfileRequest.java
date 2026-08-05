package com.locallens.dto.guide;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGuideProfileRequest {

    /*
     * Common User fields.
     */

    @NotBlank(message = "First name is required")
    @Size(
        max = 50,
        message = "First name must not exceed 50 characters"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(
        max = 100,
        message = "Last name must not exceed 100 characters"
    )
    private String lastName;

    @Pattern(
        regexp = "^$|^[0-9+\\-() ]{7,20}$",
        message = "Enter a valid phone number"
    )
    private String phoneNumber;

    @Size(
        max = 20,
        message = "Gender must not exceed 20 characters"
    )
    private String gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    /*
     * LocalGuideProfile fields.
     */

    @Size(
        max = 100,
        message = "Occupation must not exceed 100 characters"
    )
    private String occupation;

    @Min(
        value = 0,
        message = "Experience years cannot be negative"
    )
    private Integer experienceYears;

    @Size(
        max = 500,
        message = "Expertise must not exceed 500 characters"
    )
    private String expertise;

    @Size(
        max = 500,
        message = "Languages spoken must not exceed 500 characters"
    )
    private String languagesSpoken;

    private String aboutMe;

    @Size(
        max = 500,
        message = "Website URL must not exceed 500 characters"
    )
    private String websiteUrl;

    /*
     * UserDetailInfo fields.
     */

    @Size(
        max = 255,
        message = "Instagram URL must not exceed 255 characters"
    )
    private String instagramProfile;

    @Size(
        max = 255,
        message = "Facebook URL must not exceed 255 characters"
    )
    private String facebookProfile;

    /*
     * Address information.
     */

    @Valid
    private AddressRequest permanentAddress;

    private boolean currentAddressSameAsPermanent;

    @Valid
    private AddressRequest currentAddress;
}