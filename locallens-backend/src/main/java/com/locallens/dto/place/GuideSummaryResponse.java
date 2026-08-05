package com.locallens.dto.place;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GuideSummaryResponse {

    private Long userId;

    private String firstName;

    private String lastName;

    private String fullName;

    private String profileImageUrl;

    private String city;

    private String state;

    private String occupation;

    private Integer experienceYears;

    private boolean profileCompleted;

    private String verificationStatus;
}