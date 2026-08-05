package com.locallens.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.locallens.dto.guide.LocalGuideProfileResponse;
import com.locallens.dto.guide.UpdateGuideProfileRequest;
import com.locallens.entities.User;
import com.locallens.repository.UserRepository;
import com.locallens.service.LocalGuideProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/guide/profile")
@RequiredArgsConstructor
public class LocalGuideProfileController {

    private final LocalGuideProfileService
            profileService;

    private final UserRepository
            userRepository;

    /**
     * Returns the authenticated guide's private profile.
     *
     * GET /api/guide/profile
     */
    @GetMapping
    public ResponseEntity<LocalGuideProfileResponse>
            getMyProfile(
                    Authentication authentication
            ) {

        Long userId =
                getAuthenticatedUserId(
                        authentication
                );

        LocalGuideProfileResponse response =
                profileService.getMyProfile(
                        userId
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Updates the authenticated guide's profile.
     *
     * PUT /api/guide/profile
     */
    @PutMapping
    public ResponseEntity<LocalGuideProfileResponse>
            updateProfile(
                    Authentication authentication,
                    @Valid
                    @RequestBody
                    UpdateGuideProfileRequest request
            ) {

        Long userId =
                getAuthenticatedUserId(
                        authentication
                );

        LocalGuideProfileResponse response =
                profileService.updateProfile(
                        userId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Returns a public local-guide profile.
     *
     * GET /api/guide/profile/public/{guideUserId}
     */
    @GetMapping("/public/{guideUserId}")
    public ResponseEntity<LocalGuideProfileResponse>
            getPublicProfile(
                    @PathVariable Long guideUserId
            ) {

        LocalGuideProfileResponse response =
                profileService.getPublicProfile(
                        guideUserId
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Finds the authenticated user using the email
     * stored in the Spring Security authentication.
     */
    private Long getAuthenticatedUserId(
            Authentication authentication
    ) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || authentication.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Authentication is required"
            );
        }

        String authenticatedEmail =
                authentication
                        .getName()
                        .trim();

        User user = userRepository
                .findByEmailIgnoreCase(
                        authenticatedEmail
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Authenticated user not found"
                        )
                );

        return user.getId();
    }
}