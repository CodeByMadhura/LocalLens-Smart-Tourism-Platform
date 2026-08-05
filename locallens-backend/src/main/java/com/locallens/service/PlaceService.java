package com.locallens.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.locallens.dto.place.CreatePlaceRequest;
import com.locallens.dto.place.GuideSummaryResponse;
import com.locallens.dto.place.PlaceImageResponse;
import com.locallens.dto.place.PlaceResponse;
import com.locallens.dto.place.UpdatePlaceRequest;
import com.locallens.entities.LocalGuideProfile;
import com.locallens.entities.Place;
import com.locallens.entities.PlaceDetail;
import com.locallens.entities.PlaceImage;
import com.locallens.entities.User;
import com.locallens.entities.UserAddress;
import com.locallens.enums.ApprovalStatus;
import com.locallens.enums.UserRole;
import com.locallens.repository.LocalGuideProfileRepository;
import com.locallens.repository.PlaceImageRepository;
import com.locallens.repository.PlaceRepository;
import com.locallens.repository.UserAddressRepository;
import com.locallens.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private static final int MAXIMUM_PLACE_IMAGES = 8;

    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final UserRepository userRepository;
    private final LocalGuideProfileRepository guideProfileRepository;
    private final UserAddressRepository userAddressRepository;
    private final FileStorageService fileStorageService;

    // =========================================================
    // CREATE PLACE
    // =========================================================

    @Transactional
    public PlaceResponse createPlace(
            Long guideUserId,
            CreatePlaceRequest request,
            List<MultipartFile> images
    ) {

        User guide = getLocalGuide(guideUserId);

        validateCreateRequest(request);
        validateImages(images);

        Place place = new Place();

        copyRequestToPlace(request, place);

        place.setCreatedBy(guide);
        place.setStatus(ApprovalStatus.PENDING);
        place.setApprovedBy(null);
        place.setRejectionReason(null);

        Place savedPlace = placeRepository.save(place);

        addImagesToPlace(savedPlace, images);

        savedPlace = placeRepository.save(savedPlace);

        return convertToResponse(savedPlace);
    }

    // =========================================================
    // PUBLIC PLACE METHODS
    // =========================================================

    @Transactional(readOnly = true)
    public PlaceResponse getPlaceById(
            Long placeId
    ) {

        Place place = getPlaceEntity(placeId);

        return convertToResponse(place);
    }

    @Transactional(readOnly = true)
    public List<PlaceResponse> getApprovedPlaces() {

        return placeRepository
                .findByStatusOrderByCreatedAtDesc(
                        ApprovalStatus.APPROVED
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =========================================================
    // GUIDE PLACE METHODS
    // =========================================================

    @Transactional(readOnly = true)
    public List<PlaceResponse> getGuidePlaces(
            Long guideUserId
    ) {

        getLocalGuide(guideUserId);

        return placeRepository
                .findByCreatedByIdOrderByCreatedAtDesc(
                        guideUserId
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional
    public PlaceResponse updatePlace(
            Long guideUserId,
            Long placeId,
            UpdatePlaceRequest request
    ) {

        getLocalGuide(guideUserId);

        validateUpdateRequest(request);

        Place place = getOwnedPlace(
                guideUserId,
                placeId
        );

        place.setName(
                request.getName().trim()
        );

        place.setCity(
                request.getCity().trim()
        );

        place.setState(
                trimToNull(
                        request.getState()
                )
        );

        place.setCategory(
                request.getCategory().trim()
        );

        PlaceDetail details = place.getDetails();

        if (details == null) {
            details = new PlaceDetail();
            place.setDetails(details);
        }

        details.setDescription(
                request.getDescription().trim()
        );

        details.setFoodDescription(
                trimToNull(
                        request.getFoodDescription()
                )
        );

        place.setEstimatedCost(
                request.getEstimatedCost()
        );

        place.setLatitude(
                request.getLatitude()
        );

        place.setLongitude(
                request.getLongitude()
        );

        /*
         * Updated places must be approved again.
         */
        place.setStatus(ApprovalStatus.PENDING);
        place.setApprovedBy(null);
        place.setRejectionReason(null);

        Place updatedPlace =
                placeRepository.save(place);

        return convertToResponse(updatedPlace);
    }

    @Transactional
    public void deletePlace(
            Long guideUserId,
            Long placeId
    ) {

        Place place = getOwnedPlace(
                guideUserId,
                placeId
        );

        if (place.getImages() != null) {
            place.getImages()
                    .forEach(image ->
                            fileStorageService
                                    .deletePlaceImage(
                                            image.getImageUrl()
                                    )
                    );
        }

        placeRepository.delete(place);
    }

    // =========================================================
    // ADMIN METHODS
    // =========================================================

    @Transactional(readOnly = true)
    public List<PlaceResponse> getPendingPlaces(
            Long adminUserId
    ) {

        getAdmin(adminUserId);

        return placeRepository
                .findByStatusOrderByCreatedAtDesc(
                        ApprovalStatus.PENDING
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlaceResponse> getRejectedPlaces(
            Long adminUserId
    ) {

        getAdmin(adminUserId);

        return placeRepository
                .findByStatusOrderByCreatedAtDesc(
                        ApprovalStatus.REJECTED
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlaceResponse> getAdminApprovedPlaces(
            Long adminUserId
    ) {

        getAdmin(adminUserId);

        return placeRepository
                .findByStatusOrderByCreatedAtDesc(
                        ApprovalStatus.APPROVED
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional
    public PlaceResponse approvePlace(
            Long adminUserId,
            Long placeId
    ) {

        User admin = getAdmin(adminUserId);

        Place place = getPlaceEntity(placeId);

        if (
                place.getStatus()
                        == ApprovalStatus.APPROVED
        ) {
            throw new IllegalArgumentException(
                    "Place is already approved"
            );
        }

        place.setStatus(
                ApprovalStatus.APPROVED
        );

        place.setApprovedBy(admin);
        place.setRejectionReason(null);

        Place approvedPlace =
                placeRepository.save(place);

        return convertToResponse(
                approvedPlace
        );
    }

    @Transactional
    public PlaceResponse rejectPlace(
            Long adminUserId,
            Long placeId,
            String rejectionReason
    ) {

        User admin = getAdmin(adminUserId);

        validateRequiredText(
                rejectionReason,
                "Rejection reason"
        );

        Place place = getPlaceEntity(placeId);

        if (
                place.getStatus()
                        == ApprovalStatus.REJECTED
        ) {
            throw new IllegalArgumentException(
                    "Place is already rejected"
            );
        }

        place.setStatus(
                ApprovalStatus.REJECTED
        );

        place.setApprovedBy(admin);

        place.setRejectionReason(
                rejectionReason.trim()
        );

        Place rejectedPlace =
                placeRepository.save(place);

        return convertToResponse(
                rejectedPlace
        );
    }

    // =========================================================
    // PLACE IMAGE METHODS
    // =========================================================

    @Transactional
    public PlaceImageResponse addImage(
            Long guideUserId,
            Long placeId,
            MultipartFile image,
            String caption,
            boolean primaryImage
    ) {

        Place place = getOwnedPlace(
                guideUserId,
                placeId
        );

        validateSingleImage(image);

        long currentImageCount =
                placeImageRepository
                        .countByPlaceId(
                                placeId
                        );

        if (
                currentImageCount
                        >= MAXIMUM_PLACE_IMAGES
        ) {
            throw new IllegalArgumentException(
                    "A place can contain a maximum of "
                            + MAXIMUM_PLACE_IMAGES
                            + " images"
            );
        }

        String imageUrl =
                fileStorageService
                        .storePlaceImage(image);

        PlaceImage placeImage =
                new PlaceImage();

        placeImage.setPlace(place);
        placeImage.setImageUrl(imageUrl);
        placeImage.setCaption(
                trimToNull(caption)
        );

        if (
                currentImageCount == 0
                || primaryImage
        ) {

            clearCurrentPrimaryImage(
                    placeId
            );

            placeImage.setPrimaryImage(
                    true
            );

        } else {

            placeImage.setPrimaryImage(
                    false
            );
        }

        PlaceImage savedImage =
                placeImageRepository.save(
                        placeImage
                );

        /*
         * Image changes require approval again.
         */
        place.setStatus(
                ApprovalStatus.PENDING
        );

        place.setApprovedBy(null);
        place.setRejectionReason(null);

        placeRepository.save(place);

        return convertImageToResponse(
                savedImage
        );
    }

    @Transactional
    public void deleteImage(
            Long guideUserId,
            Long placeId,
            Long imageId
    ) {

        Place place = getOwnedPlace(
                guideUserId,
                placeId
        );

        if (imageId == null) {
            throw new IllegalArgumentException(
                    "Image ID cannot be null"
            );
        }

        PlaceImage image =
                placeImageRepository
                        .findByIdAndPlaceId(
                                imageId,
                                placeId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Place image not found"
                                )
                        );

        boolean primaryImage =
                image.isPrimaryImage();

        String imageUrl =
                image.getImageUrl();

        placeImageRepository.delete(image);

        fileStorageService
                .deletePlaceImage(
                        imageUrl
                );

        if (primaryImage) {

            placeImageRepository
                    .findByPlaceIdOrderByPrimaryImageDescIdAsc(
                            placeId
                    )
                    .stream()
                    .findFirst()
                    .ifPresent(nextImage -> {

                        nextImage.setPrimaryImage(
                                true
                        );

                        placeImageRepository.save(
                                nextImage
                        );
                    });
        }

        place.setStatus(
                ApprovalStatus.PENDING
        );

        place.setApprovedBy(null);
        place.setRejectionReason(null);

        placeRepository.save(place);
    }

    private void addImagesToPlace(
            Place place,
            List<MultipartFile> images
    ) {

        if (
                images == null
                || images.isEmpty()
        ) {
            return;
        }

        List<MultipartFile> validImages =
                images.stream()
                        .filter(file ->
                                file != null
                                        && !file.isEmpty()
                        )
                        .toList();

        for (
                int index = 0;
                index < validImages.size();
                index++
        ) {

            MultipartFile image =
                    validImages.get(index);

            String imageUrl =
                    fileStorageService
                            .storePlaceImage(
                                    image
                            );

            PlaceImage placeImage =
                    new PlaceImage();

            placeImage.setImageUrl(
                    imageUrl
            );

            placeImage.setCaption(null);

            placeImage.setPrimaryImage(
                    index == 0
            );

            /*
             * Place.addImage() should set:
             * placeImage.setPlace(place)
             */
            place.addImage(placeImage);
        }
    }

    private void clearCurrentPrimaryImage(
            Long placeId
    ) {

        placeImageRepository
                .findByPlaceIdAndPrimaryImageTrue(
                        placeId
                )
                .ifPresent(image -> {

                    image.setPrimaryImage(
                            false
                    );

                    placeImageRepository.save(
                            image
                    );
                });
    }

    // =========================================================
    // VALIDATION METHODS
    // =========================================================

    private void validateCreateRequest(
            CreatePlaceRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Create place request cannot be null"
            );
        }

        validateRequiredText(
                request.getName(),
                "Place name"
        );

        validateRequiredText(
                request.getCity(),
                "City"
        );

        validateRequiredText(
                request.getCategory(),
                "Category"
        );

        validateRequiredText(
                request.getDescription(),
                "Description"
        );

        validateCoordinates(
                request.getLatitude(),
                request.getLongitude()
        );
    }

    private void validateUpdateRequest(
            UpdatePlaceRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Update place request cannot be null"
            );
        }

        validateRequiredText(
                request.getName(),
                "Place name"
        );

        validateRequiredText(
                request.getCity(),
                "City"
        );

        validateRequiredText(
                request.getCategory(),
                "Category"
        );

        validateRequiredText(
                request.getDescription(),
                "Description"
        );

        validateCoordinates(
                request.getLatitude(),
                request.getLongitude()
        );
    }

    private void validateImages(
            List<MultipartFile> images
    ) {

        if (
                images == null
                || images.isEmpty()
        ) {
            return;
        }

        List<MultipartFile> validImages =
                images.stream()
                        .filter(file ->
                                file != null
                                        && !file.isEmpty()
                        )
                        .toList();

        if (
                validImages.size()
                        > MAXIMUM_PLACE_IMAGES
        ) {
            throw new IllegalArgumentException(
                    "Maximum "
                            + MAXIMUM_PLACE_IMAGES
                            + " images are allowed"
            );
        }

        validImages.forEach(
                this::validateSingleImage
        );
    }

    private void validateSingleImage(
            MultipartFile image
    ) {

        if (
                image == null
                || image.isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "Image file cannot be empty"
            );
        }

        String contentType =
                image.getContentType();

        if (
                contentType == null
                || !contentType.startsWith(
                        "image/"
                )
        ) {
            throw new IllegalArgumentException(
                    "Only image files are allowed"
            );
        }
    }

    private void validateCoordinates(
            BigDecimal latitude,
            BigDecimal longitude
    ) {

        if (latitude == null) {
            throw new IllegalArgumentException(
                    "Latitude is required"
            );
        }

        if (longitude == null) {
            throw new IllegalArgumentException(
                    "Longitude is required"
            );
        }

        if (
                latitude.compareTo(
                        BigDecimal.valueOf(-90)
                ) < 0
                || latitude.compareTo(
                        BigDecimal.valueOf(90)
                ) > 0
        ) {
            throw new IllegalArgumentException(
                    "Latitude must be between -90 and 90"
            );
        }

        if (
                longitude.compareTo(
                        BigDecimal.valueOf(-180)
                ) < 0
                || longitude.compareTo(
                        BigDecimal.valueOf(180)
                ) > 0
        ) {
            throw new IllegalArgumentException(
                    "Longitude must be between -180 and 180"
            );
        }
    }

    private void validateRequiredText(
            String value,
            String fieldName
    ) {

        if (
                value == null
                || value.isBlank()
        ) {
            throw new IllegalArgumentException(
                    fieldName + " is required"
            );
        }
    }

    // =========================================================
    // LOOKUP METHODS
    // =========================================================

    private User getLocalGuide(
            Long userId
    ) {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "Guide user ID cannot be null"
            );
        }

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "User not found with ID: "
                                                + userId
                                )
                        );

        if (
                user.getRole()
                        != UserRole.LOCAL_GUIDE
        ) {
            throw new IllegalArgumentException(
                    "Only local guides can perform this operation"
            );
        }

        if (!user.isActive()) {
            throw new IllegalArgumentException(
                    "User account is inactive"
            );
        }

        return user;
    }

    private User getAdmin(
            Long adminUserId
    ) {

        if (adminUserId == null) {
            throw new IllegalArgumentException(
                    "Admin user ID cannot be null"
            );
        }

        User admin =
                userRepository
                        .findById(adminUserId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Admin not found with ID: "
                                                + adminUserId
                                )
                        );

        if (
                admin.getRole()
                        != UserRole.ADMIN
        ) {
            throw new IllegalArgumentException(
                    "Only admins can perform this operation"
            );
        }

        if (!admin.isActive()) {
            throw new IllegalArgumentException(
                    "Admin account is inactive"
            );
        }

        return admin;
    }

    private Place getPlaceEntity(
            Long placeId
    ) {

        if (placeId == null) {
            throw new IllegalArgumentException(
                    "Place ID cannot be null"
            );
        }

        /*
         * Loads images, creator and details together.
         */
        return placeRepository
                .findWithImagesById(placeId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Place not found with ID: "
                                        + placeId
                        )
                );
    }

    private Place getOwnedPlace(
            Long guideUserId,
            Long placeId
    ) {

        Place place =
                getPlaceEntity(placeId);

        if (
                place.getCreatedBy() == null
                || place.getCreatedBy()
                        .getId() == null
                || !place.getCreatedBy()
                        .getId()
                        .equals(guideUserId)
        ) {
            throw new IllegalArgumentException(
                    "You are not allowed to modify this place"
            );
        }

        return place;
    }

    // =========================================================
    // ENTITY MAPPING
    // =========================================================

    private void copyRequestToPlace(
            CreatePlaceRequest request,
            Place place
    ) {

        place.setName(
                request.getName().trim()
        );

        place.setCity(
                request.getCity().trim()
        );

        place.setState(
                trimToNull(
                        request.getState()
                )
        );

        place.setCategory(
                request.getCategory().trim()
        );

        PlaceDetail details =
                new PlaceDetail();

        details.setDescription(
                request.getDescription().trim()
        );

        details.setFoodDescription(
                trimToNull(
                        request.getFoodDescription()
                )
        );

        place.setDetails(details);

        place.setEstimatedCost(
                request.getEstimatedCost()
        );

        place.setLatitude(
                request.getLatitude()
        );

        place.setLongitude(
                request.getLongitude()
        );
    }

    private PlaceResponse convertToResponse(
            Place place
    ) {

        List<PlaceImageResponse> imageResponses =
                place.getImages() == null
                        ? new ArrayList<>()
                        : place.getImages()
                                .stream()
                                .filter(image ->
                                        image != null
                                )
                                .map(
                                        this::convertImageToResponse
                                )
                                .toList();

        return PlaceResponse.builder()
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

                .latitude(
                        place.getLatitude()
                )

                .longitude(
                        place.getLongitude()
                )

                .averageRating(
                        place.getAverageRating()
                )

                .reviewCount(
                        place.getReviewCount()
                )

                .status(
                        place.getStatus() == null
                                ? null
                                : place.getStatus()
                                        .name()
                )

                .rejectionReason(
                        place.getRejectionReason()
                )

                .images(imageResponses)

                .createdBy(
                        place.getCreatedBy() == null
                                ? null
                                : convertGuideSummary(
                                        place.getCreatedBy()
                                )
                )

                .createdAt(
                        place.getCreatedAt()
                )

                .updatedAt(
                        place.getUpdatedAt()
                )

                .build();
    }

    private PlaceImageResponse convertImageToResponse(
            PlaceImage image
    ) {

        return PlaceImageResponse.builder()
                .id(image.getId())
                .imageUrl(
                        normalizeImageUrl(
                                image.getImageUrl()
                        )
                )
                .primaryImage(
                        image.isPrimaryImage()
                )
                .caption(
                        image.getCaption()
                )
                .build();
    }

    private GuideSummaryResponse convertGuideSummary(
            User user
    ) {

        LocalGuideProfile profile =
                guideProfileRepository
                        .findByUserId(
                                user.getId()
                        )
                        .orElse(null);

        UserAddress address =
                userAddressRepository
                        .findFirstByUserIdAndCurrentTrue(
                                user.getId()
                        )
                        .orElse(null);

        String firstName =
                trimToEmpty(
                        user.getFirstName()
                );

        String lastName =
                trimToEmpty(
                        user.getLastName()
                );

        String fullName =
                (firstName + " " + lastName)
                        .trim();

        return GuideSummaryResponse.builder()
                .userId(user.getId())
                .firstName(
                        user.getFirstName()
                )
                .lastName(
                        user.getLastName()
                )
                .fullName(fullName)
                .profileImageUrl(null)

                .city(
                        address == null
                                ? null
                                : address.getCity()
                )

                .state(
                        address == null
                                ? null
                                : address.getState()
                )

                .occupation(
                        profile == null
                                ? null
                                : profile.getOccupation()
                )

                .experienceYears(
                        profile == null
                                ? null
                                : profile.getExperienceYears()
                )

                .profileCompleted(
                        profile != null
                                && profile.isProfileCompleted()
                )

                .verificationStatus(
                        profile == null
                                || profile.getVerificationStatus()
                                        == null
                                ? null
                                : profile.getVerificationStatus()
                                        .name()
                )

                .build();
    }

    // =========================================================
    // IMAGE URL HELPER
    // =========================================================

    private String normalizeImageUrl(
            String imageUrl
    ) {

        String value =
                trimToNull(imageUrl);

        if (value == null) {
            return null;
        }

        if (
                value.startsWith("http://")
                || value.startsWith("https://")
                || value.startsWith("/")
        ) {
            return value;
        }

        if (value.startsWith("uploads/")) {
            return "/" + value;
        }

        return "/uploads/places/" + value;
    }

    // =========================================================
    // STRING HELPERS
    // =========================================================

    private String trimToNull(
            String value
    ) {

        if (
                value == null
                || value.isBlank()
        ) {
            return null;
        }

        return value.trim();
    }

    private String trimToEmpty(
            String value
    ) {

        return value == null
                ? ""
                : value.trim();
    }
}