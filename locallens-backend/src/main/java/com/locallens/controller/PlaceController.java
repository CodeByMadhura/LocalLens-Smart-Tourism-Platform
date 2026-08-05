package com.locallens.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.locallens.dto.place.CreatePlaceRequest;
import com.locallens.dto.place.PlaceImageResponse;
import com.locallens.dto.place.PlaceResponse;
import com.locallens.dto.place.UpdatePlaceRequest;
import com.locallens.entities.User;
import com.locallens.repository.UserRepository;
import com.locallens.service.PlaceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final UserRepository userRepository;

    // =========================================================
    // LOCAL GUIDE PLACE ENDPOINTS
    // =========================================================

    /**
     * Creates a new place for the authenticated local guide.
     *
     * POST /api/guide/places
     */
    @PostMapping(
            value = "/guide/places",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<PlaceResponse> createPlace(
            Authentication authentication,

            @Valid
            @RequestPart("place")
            CreatePlaceRequest request,

            @RequestPart(
                    value = "images",
                    required = false
            )
            List<MultipartFile> images
    ) {

        Long guideUserId =
                getAuthenticatedUserId(authentication);

        List<MultipartFile> safeImages =
                images == null
                        ? Collections.emptyList()
                        : images;

        PlaceResponse response =
                placeService.createPlace(
                        guideUserId,
                        request,
                        safeImages
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Returns all places created by the authenticated guide.
     *
     * GET /api/guide/places
     */
    @GetMapping("/guide/places")
    public ResponseEntity<List<PlaceResponse>> getMyPlaces(
            Authentication authentication
    ) {

        Long guideUserId =
                getAuthenticatedUserId(authentication);

        List<PlaceResponse> places =
                placeService.getGuidePlaces(guideUserId);

        return ResponseEntity.ok(places);
    }

    /**
     * Updates a place belonging to the authenticated guide.
     *
     * PUT /api/guide/places/{placeId}
     */
    @PutMapping(
            value = "/guide/places/{placeId:\\d+}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PlaceResponse> updatePlace(
            Authentication authentication,

            @PathVariable("placeId")
            Long placeId,

            @Valid
            @RequestBody
            UpdatePlaceRequest request
    ) {

        Long guideUserId =
                getAuthenticatedUserId(authentication);

        PlaceResponse response =
                placeService.updatePlace(
                        guideUserId,
                        placeId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Adds an image to a guide's place.
     *
     * POST /api/guide/places/{placeId}/images
     */
    @PostMapping(
            value = "/guide/places/{placeId:\\d+}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<PlaceImageResponse> addPlaceImage(
            Authentication authentication,

            @PathVariable("placeId")
            Long placeId,

            @RequestParam("image")
            MultipartFile image,

            @RequestParam(
                    value = "caption",
                    required = false
            )
            String caption,

            @RequestParam(
                    value = "primaryImage",
                    defaultValue = "false"
            )
            boolean primaryImage
    ) {

        Long guideUserId =
                getAuthenticatedUserId(authentication);

        validateImage(image);

        PlaceImageResponse response =
                placeService.addImage(
                        guideUserId,
                        placeId,
                        image,
                        caption,
                        primaryImage
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Deletes an image from a guide's place.
     *
     * DELETE /api/guide/places/{placeId}/images/{imageId}
     */
    @DeleteMapping(
            "/guide/places/{placeId:\\d+}/images/{imageId:\\d+}"
    )
    public ResponseEntity<Void> deletePlaceImage(
            Authentication authentication,

            @PathVariable("placeId")
            Long placeId,

            @PathVariable("imageId")
            Long imageId
    ) {

        Long guideUserId =
                getAuthenticatedUserId(authentication);

        placeService.deleteImage(
                guideUserId,
                placeId,
                imageId
        );

        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a place belonging to the authenticated guide.
     *
     * DELETE /api/guide/places/{placeId}
     */
    @DeleteMapping("/guide/places/{placeId:\\d+}")
    public ResponseEntity<Void> deletePlace(
            Authentication authentication,

            @PathVariable("placeId")
            Long placeId
    ) {

        Long guideUserId =
                getAuthenticatedUserId(authentication);

        placeService.deletePlace(
                guideUserId,
                placeId
        );

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // PUBLIC / TRAVELLER PLACE ENDPOINTS
    // =========================================================

    /**
     * Returns all approved places.
     *
     * GET /api/places/approved
     */
    @GetMapping("/places/approved")
    public ResponseEntity<List<PlaceResponse>> getApprovedPlaces() {

        List<PlaceResponse> places =
                placeService.getApprovedPlaces();

        return ResponseEntity.ok(places);
    }

    /**
     * Optional alternative endpoint.
     *
     * GET /api/places
     *
     * This is kept so existing frontend pages using
     * /api/places continue working.
     */
    @GetMapping("/places")
    public ResponseEntity<List<PlaceResponse>>
            getAllApprovedPlaces() {

        List<PlaceResponse> places =
                placeService.getApprovedPlaces();

        return ResponseEntity.ok(places);
    }

    /**
     * Returns one place using its numeric ID.
     *
     * GET /api/places/1
     */
    @GetMapping("/places/{placeId:\\d+}")
    public ResponseEntity<PlaceResponse> getPlace(
            @PathVariable("placeId")
            Long placeId
    ) {

        PlaceResponse response =
                placeService.getPlaceById(placeId);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // AUTHENTICATION HELPER
    // =========================================================

    private Long getAuthenticatedUserId(
            Authentication authentication
    ) {

        if (
                authentication == null
                        || !authentication.isAuthenticated()
        ) {
            throw new IllegalArgumentException(
                    "Authentication is required"
            );
        }

        String email = authentication.getName();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Authenticated user email is missing"
            );
        }

        User user =
                userRepository
                        .findByEmailIgnoreCase(email.trim())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Authenticated user was not found"
                                )
                        );

        return user.getId();
    }

    // =========================================================
    // IMAGE VALIDATION HELPER
    // =========================================================

    private void validateImage(
            MultipartFile image
    ) {

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select a valid image file"
            );
        }

        String contentType = image.getContentType();

        if (
                contentType == null
                        || !contentType.startsWith("image/")
        ) {
            throw new IllegalArgumentException(
                    "Only image files are allowed"
            );
        }
    }
}