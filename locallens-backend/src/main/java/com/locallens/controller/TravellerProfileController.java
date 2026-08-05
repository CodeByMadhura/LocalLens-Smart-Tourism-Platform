package com.locallens.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.locallens.dto.traveller.TravellerProfileResponse;
import com.locallens.dto.traveller.UpdateTravellerProfileRequest;
import com.locallens.service.TravellerProfileService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("/api/traveller/profile")
@RequiredArgsConstructor
public class TravellerProfileController {

    private final TravellerProfileService travellerProfileService;

    /**
     * Load the currently logged-in traveller profile.
     *
     * GET /api/traveller/profile
     */
    @GetMapping
    public ResponseEntity<TravellerProfileResponse> getProfile(
            Authentication authentication
    ) {
        String email = getAuthenticatedEmail(authentication);

        TravellerProfileResponse response =
                travellerProfileService.getProfileByEmail(email);

        return ResponseEntity.ok(response);
    }

    /**
     * Create or update the currently logged-in traveller profile.
     *
     * PUT /api/traveller/profile
     */
    @PutMapping
    public ResponseEntity<TravellerProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateTravellerProfileRequest request
    ) {
        String email = getAuthenticatedEmail(authentication);

        TravellerProfileResponse response =
                travellerProfileService.updateProfileByEmail(
                        email,
                        request
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Update the traveller profile-image URL.
     *
     * PATCH /api/traveller/profile/image
     */
    @PatchMapping("/image")
    public ResponseEntity<TravellerProfileResponse> updateProfileImage(
            Authentication authentication,
            @Valid @RequestBody ProfileImageRequest request
    ) {
        String email = getAuthenticatedEmail(authentication);

        TravellerProfileResponse response =
                travellerProfileService.updateProfileImage(
                        email,
                        request.getProfileImageUrl()
                );

        return ResponseEntity.ok(response);
    }

    private String getAuthenticatedEmail(
            Authentication authentication
    ) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || authentication.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Authenticated traveller was not found"
            );
        }

        return authentication.getName().trim();
    }

    @Getter
    @Setter
    public static class ProfileImageRequest {

        @NotBlank(message = "Profile image URL is required")
        private String profileImageUrl;
    }
}