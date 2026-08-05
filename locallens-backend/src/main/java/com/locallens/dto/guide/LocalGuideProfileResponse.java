package com.locallens.dto.guide;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.locallens.dto.place.PlaceResponse;

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
public class LocalGuideProfileResponse {

    /*
     * LocalGuideProfile table ID.
     */
    private Long profileId;

    /*
     * Users table ID.
     */
    private Long userId;

    /*
     * Common user information.
     */
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;

    /*
     * Profile picture stored in user_photos.
     */
    private String profileImageUrl;

    /*
     * Guide-specific information.
     */
    private String aboutMe;
    private Integer experienceYears;
    private String occupation;
    private String expertise;
    private String languagesSpoken;
    private String websiteUrl;

    /*
     * Common social information stored in user_detail_info.
     */
    private String instagramProfile;
    private String facebookProfile;

    /*
     * Guide verification information.
     */
    private String verificationDocumentUrl;
    private String verificationStatus;
    private boolean profileCompleted;

    /*
     * True when the current address is represented by
     * the permanent address row.
     */
    private boolean currentAddressSameAsPermanent;

    @Builder.Default
    private List<AddressResponse> addresses =
            new ArrayList<>();

    /*
     * Places submitted by the guide.
     */
    @Builder.Default
    private List<PlaceResponse> places =
            new ArrayList<>();
}