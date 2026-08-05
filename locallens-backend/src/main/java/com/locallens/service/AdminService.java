package com.locallens.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.locallens.dto.admin.AdminDashboardResponse;
import com.locallens.dto.admin.AdminUserResponse;
import com.locallens.dto.place.GuideSummaryResponse;
import com.locallens.dto.place.PlaceImageResponse;
import com.locallens.dto.place.PlaceResponse;
import com.locallens.entities.Place;
import com.locallens.entities.User;
import com.locallens.enums.ApprovalStatus;
import com.locallens.enums.UserRole;
import com.locallens.repository.PlaceRepository;
import com.locallens.repository.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public AdminService(
            UserRepository userRepository,
            PlaceRepository placeRepository
    ) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    // =========================================================
    // USER LISTING
    // =========================================================

    @Transactional(readOnly = true)
    public List<AdminUserResponse> getAllUsers() {

        return userRepository
                .findAll()
                .stream()
                .filter(user ->
                        user.getRole() != UserRole.ADMIN
                )
                .map(this::convertUserToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AdminUserResponse> getTravellers() {

        return userRepository
                .findByRoleOrderByIdDesc(
                        UserRole.TRAVELLER
                )
                .stream()
                .map(this::convertUserToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AdminUserResponse> getLocalGuides() {

        return userRepository
                .findByRoleOrderByIdDesc(
                        UserRole.LOCAL_GUIDE
                )
                .stream()
                .map(this::convertUserToResponse)
                .toList();
    }

    // =========================================================
    // USER STATUS
    // =========================================================

    @Transactional
    public AdminUserResponse updateUserStatus(
            Long userId,
            boolean active
    ) {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID cannot be null."
            );
        }

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with ID: "
                                        + userId
                        )
                );

        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException(
                    "Admin account status cannot be changed."
            );
        }

        user.setActive(active);

        return convertUserToResponse(
                userRepository.save(user)
        );
    }

    // =========================================================
    // PLACE LISTING
    // =========================================================

    @Transactional(readOnly = true)
    public List<PlaceResponse> getPendingPlaces() {

        return placeRepository
                .findByStatusOrderByCreatedAtDesc(
                        ApprovalStatus.PENDING
                )
                .stream()
                .map(this::convertPlaceToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlaceResponse> getApprovedPlaces() {

        return placeRepository
                .findByStatusOrderByCreatedAtDesc(
                        ApprovalStatus.APPROVED
                )
                .stream()
                .map(this::convertPlaceToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlaceResponse> getRejectedPlaces() {

        return placeRepository
                .findByStatusOrderByCreatedAtDesc(
                        ApprovalStatus.REJECTED
                )
                .stream()
                .map(this::convertPlaceToResponse)
                .toList();
    }

    // =========================================================
    // APPROVE PLACE
    // =========================================================

    @Transactional
    public PlaceResponse approvePlace(
            Long placeId
    ) {

        Place place = getPlaceById(placeId);

        place.setStatus(
                ApprovalStatus.APPROVED
        );

        place.setRejectionReason(null);

        Place savedPlace =
                placeRepository.save(place);

        return convertPlaceToResponse(
                savedPlace
        );
    }

    // =========================================================
    // REJECT PLACE
    // =========================================================

    @Transactional
    public PlaceResponse rejectPlace(
            Long placeId,
            String reason
    ) {

        Place place = getPlaceById(placeId);

        String rejectionReason =
                reason == null
                        ? ""
                        : reason.trim();

        if (rejectionReason.length() < 3) {
            throw new IllegalArgumentException(
                    "Rejection reason must contain at least 3 characters."
            );
        }

        if (rejectionReason.length() > 500) {
            throw new IllegalArgumentException(
                    "Rejection reason cannot exceed 500 characters."
            );
        }

        place.setStatus(
                ApprovalStatus.REJECTED
        );

        place.setRejectionReason(
                rejectionReason
        );

        Place savedPlace =
                placeRepository.save(place);

        return convertPlaceToResponse(
                savedPlace
        );
    }

    private Place getPlaceById(
            Long placeId
    ) {

        if (placeId == null) {
            throw new IllegalArgumentException(
                    "Place ID cannot be null."
            );
        }

        return placeRepository
                .findById(placeId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Place not found with ID: "
                                        + placeId
                        )
                );
    }

    // =========================================================
    // DASHBOARD SUMMARY
    // =========================================================

    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboardSummary() {

        long totalTravellers =
                userRepository.countByRole(
                        UserRole.TRAVELLER
                );

        long totalLocalGuides =
                userRepository.countByRole(
                        UserRole.LOCAL_GUIDE
                );

        long totalUsers =
                totalTravellers +
                totalLocalGuides;

        long activeUsers =
                userRepository.countByActiveTrue();

        long pendingPlaces =
                placeRepository.countByStatus(
                        ApprovalStatus.PENDING
                );

        long approvedPlaces =
                placeRepository.countByStatus(
                        ApprovalStatus.APPROVED
                );

        long rejectedPlaces =
                placeRepository.countByStatus(
                        ApprovalStatus.REJECTED
                );

        List<AdminUserResponse> latestUsers =
                userRepository
                        .findTop5ByOrderByIdDesc()
                        .stream()
                        .filter(user ->
                                user.getRole() !=
                                        UserRole.ADMIN
                        )
                        .map(this::convertUserToResponse)
                        .toList();

        List<PlaceResponse> latestPlaces =
                placeRepository
                        .findTop5ByOrderByCreatedAtDesc()
                        .stream()
                        .map(this::convertPlaceToResponse)
                        .toList();

        return AdminDashboardResponse
                .builder()
                .totalUsers(totalUsers)
                .totalTravellers(totalTravellers)
                .totalLocalGuides(totalLocalGuides)
                .activeUsers(activeUsers)
                .pendingPlaces(pendingPlaces)
                .approvedPlaces(approvedPlaces)
                .rejectedPlaces(rejectedPlaces)
                .latestUsers(latestUsers)
                .latestPlaces(latestPlaces)
                .build();
    }

    // =========================================================
    // USER RESPONSE
    // =========================================================

    private AdminUserResponse convertUserToResponse(
            User user
    ) {

        String firstName =
                safe(user.getFirstName());

        String lastName =
                safe(user.getLastName());

        String fullName =
                (firstName + " " + lastName)
                        .trim();

        String gender =
                user.getGender() == null
                        ? null
                        : user.getGender().name();

        String role =
                user.getRole() == null
                        ? null
                        : user.getRole().name();

        return AdminUserResponse
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(fullName)
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .gender(gender)
                .role(role)
                .active(user.isActive())
                .emailVerified(
                        user.isEmailVerified()
                )
                .profileImageUrl(null)
                .build();
    }

    // =========================================================
    // PLACE RESPONSE
    // =========================================================

    private PlaceResponse convertPlaceToResponse(
            Place place
    ) {

        List<PlaceImageResponse> images =
                place.getImages() == null
                        ? List.of()
                        : place.getImages()
                                .stream()
                                .map(image ->
                                        PlaceImageResponse
                                                .builder()
                                                .id(
                                                        image.getId()
                                                )
                                                .imageUrl(
                                                        image.getImageUrl()
                                                )
                                                .primaryImage(
                                                        image.isPrimaryImage()
                                                )
                                                .caption(
                                                        image.getCaption()
                                                )
                                                .build()
                                )
                                .toList();

        User guide =
                place.getCreatedBy();

        GuideSummaryResponse guideResponse =
                guide == null
                        ? null
                        : GuideSummaryResponse
                                .builder()
                                .userId(
                                        guide.getId()
                                )
                                .firstName(
                                        guide.getFirstName()
                                )
                                .lastName(
                                        guide.getLastName()
                                )
                                .fullName(
                                        (
                                                safe(
                                                        guide.getFirstName()
                                                )
                                                        + " "
                                                        + safe(
                                                                guide.getLastName()
                                                        )
                                        ).trim()
                                )
                                .profileImageUrl(null)
                                .build();

        return PlaceResponse
                .builder()
                .id(place.getId())
                .name(place.getName())
                .city(place.getCity())
                .state(place.getState())
                .category(place.getCategory())
                .description(
                        place.getDetails() == null
                                ? null
                                : place.getDetails()
                                        .getDescription()
                )
                .foodDescription(
                        place.getDetails() == null
                                ? null
                                : place.getDetails()
                                        .getFoodDescription()
                )
                .estimatedCost(
                        place.getEstimatedCost()
                )
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .averageRating(
                        place.getAverageRating()
                )
                .reviewCount(
                        place.getReviewCount()
                )
                .status(
                        place.getStatus() == null
                                ? null
                                : place.getStatus().name()
                )
                .rejectionReason(
                        place.getRejectionReason()
                )
                .images(images)
                .createdBy(guideResponse)
                .createdAt(place.getCreatedAt())
                .updatedAt(place.getUpdatedAt())
                .build();
    }

    private String safe(String value) {

        return value == null
                ? ""
                : value.trim();
    }
}