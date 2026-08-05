package com.locallens.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.locallens.dto.guide.AddressRequest;
import com.locallens.dto.guide.AddressResponse;
import com.locallens.dto.guide.LocalGuideProfileResponse;
import com.locallens.dto.guide.UpdateGuideProfileRequest;
import com.locallens.dto.place.PlaceResponse;
import com.locallens.entities.LocalGuideProfile;
import com.locallens.entities.User;
import com.locallens.entities.UserAddress;
import com.locallens.entities.UserDetailInfo;
import com.locallens.entities.UserPhoto;
import com.locallens.enums.AddressType;
import com.locallens.enums.GenderType;
import com.locallens.enums.UserRole;
import com.locallens.enums.VerificationStatus;
import com.locallens.repository.LocalGuideProfileRepository;
import com.locallens.repository.UserAddressRepository;
import com.locallens.repository.UserDetailInfoRepository;
import com.locallens.repository.UserPhotoRepository;
import com.locallens.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocalGuideProfileService {

    private final UserRepository userRepository;

    private final LocalGuideProfileRepository
            profileRepository;

    private final UserAddressRepository
            addressRepository;

    private final UserDetailInfoRepository
            detailInfoRepository;

    private final UserPhotoRepository
            photoRepository;

    private final PlaceService placeService;

    /**
     * Creates or updates the authenticated local guide's
     * complete profile.
     */
    @Transactional
    public LocalGuideProfileResponse updateProfile(
            Long guideUserId,
            UpdateGuideProfileRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Profile update request cannot be null"
            );
        }

        User user = getGuide(guideUserId);

        /*
         * Update fields stored in users table.
         */
        updateUserDetails(user, request);

        /*
         * Create or update local_guide_profiles row.
         */
        LocalGuideProfile profile =
                profileRepository
                        .findByUserId(guideUserId)
                        .orElseGet(() ->
                                createGuideProfile(user)
                        );

        updateGuideDetails(profile, request);

        /*
         * Create or update user_detail_info row.
         */
        UserDetailInfo detailInfo =
                detailInfoRepository
                        .findByUserId(guideUserId)
                        .orElseGet(() ->
                                createUserDetailInfo(user)
                        );

        updateSocialDetails(detailInfo, request);

        /*
         * Create or update permanent/current addresses.
         */
        updateAddresses(user, request);

        boolean profileCompleted =
                calculateProfileCompletion(
                        user,
                        profile,
                        request
                );

        profile.setProfileCompleted(
                profileCompleted
        );

        user.setProfileCompleted(
                profileCompleted
        );

        userRepository.save(user);
        detailInfoRepository.save(detailInfo);

        LocalGuideProfile savedProfile =
                profileRepository.save(profile);

        return convertToResponse(
                user,
                savedProfile,
                true
        );
    }

    /**
     * Returns the authenticated local guide's private profile.
     */
    @Transactional(readOnly = true)
    public LocalGuideProfileResponse getMyProfile(
            Long guideUserId
    ) {

        User user = getGuide(guideUserId);

        LocalGuideProfile profile =
                profileRepository
                        .findByUserId(guideUserId)
                        .orElse(null);

        return convertToResponse(
                user,
                profile,
                true
        );
    }

    /**
     * Returns profile information that may be displayed
     * publicly.
     */
    @Transactional(readOnly = true)
    public LocalGuideProfileResponse getPublicProfile(
            Long guideUserId
    ) {

        User user = getGuide(guideUserId);

        LocalGuideProfile profile =
                profileRepository
                        .findByUserId(guideUserId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Local guide profile not found"
                                )
                        );

        return convertToResponse(
                user,
                profile,
                false
        );
    }

    /**
     * Updates common user fields.
     */
    private void updateUserDetails(
            User user,
            UpdateGuideProfileRequest request
    ) {

        user.setFirstName(
                requireTrimmed(
                        request.getFirstName(),
                        "First name is required"
                )
        );

        user.setLastName(
                requireTrimmed(
                        request.getLastName(),
                        "Last name is required"
                )
        );

        String newPhoneNumber =
                trimToNull(
                        request.getPhoneNumber()
                );

        /*
         * Check duplicate phone number only when the number
         * is different from the user's existing number.
         */
        if (newPhoneNumber != null
                && !Objects.equals(
                        normalizePhone(
                                user.getPhoneNumber()
                        ),
                        normalizePhone(
                                newPhoneNumber
                        )
                )
                && userRepository.existsByPhoneNumber(
                        newPhoneNumber
                )) {

            throw new IllegalArgumentException(
                    "Phone number is already registered"
            );
        }

        user.setPhoneNumber(newPhoneNumber);

        user.setGender(
                parseGender(
                        request.getGender()
                )
        );

        user.setDateOfBirth(
                request.getDateOfBirth()
        );
    }

    /**
     * Creates a new local guide profile.
     */
    private LocalGuideProfile createGuideProfile(
            User user
    ) {

        LocalGuideProfile profile =
                new LocalGuideProfile();

        profile.setUser(user);

        profile.setVerificationStatus(
                VerificationStatus.PENDING
        );

        profile.setProfileCompleted(false);

        user.setLocalGuideProfile(profile);

        return profile;
    }

    /**
     * Updates guide-specific fields.
     */
    private void updateGuideDetails(
            LocalGuideProfile profile,
            UpdateGuideProfileRequest request
    ) {

        profile.setOccupation(
                trimToNull(
                        request.getOccupation()
                )
        );

        profile.setExperienceYears(
                request.getExperienceYears()
        );

        profile.setExpertise(
                trimToNull(
                        request.getExpertise()
                )
        );

        profile.setLanguagesSpoken(
                trimToNull(
                        request.getLanguagesSpoken()
                )
        );

        profile.setAboutMe(
                trimToNull(
                        request.getAboutMe()
                )
        );

        profile.setWebsiteUrl(
                trimToNull(
                        request.getWebsiteUrl()
                )
        );
    }

    /**
     * Creates common user details.
     */
    private UserDetailInfo createUserDetailInfo(
            User user
    ) {

        UserDetailInfo detailInfo =
                new UserDetailInfo();

        detailInfo.setUser(user);

        user.setDetailInfo(detailInfo);

        return detailInfo;
    }

    /**
     * Updates social profile fields.
     */
    private void updateSocialDetails(
            UserDetailInfo detailInfo,
            UpdateGuideProfileRequest request
    ) {

        detailInfo.setInstagramProfile(
                trimToNull(
                        request.getInstagramProfile()
                )
        );

        detailInfo.setFacebookProfile(
                trimToNull(
                        request.getFacebookProfile()
                )
        );
    }

    /**
     * Creates or updates permanent and current addresses.
     */
    private void updateAddresses(
            User user,
            UpdateGuideProfileRequest request
    ) {

        AddressRequest permanentRequest =
                request.getPermanentAddress();

        /*
         * Allow the guide to save a partially completed
         * profile without an address.
         */
        if (permanentRequest == null) {
            return;
        }

        /*
         * Create or update permanent address.
         */
        UserAddress permanentAddress =
                getOrCreateAddress(
                        user,
                        AddressType.PERMANENT
                );

        copyAddress(
                permanentRequest,
                permanentAddress
        );

        /*
         * When current address is the same as permanent,
         * mark permanent address as current and remove any
         * previously stored separate current address.
         */
        if (request.isCurrentAddressSameAsPermanent()) {

            permanentAddress.setCurrent(true);

            addressRepository.save(
                    permanentAddress
            );

            addressRepository
                    .deleteByUserIdAndAddressType(
                            user.getId(),
                            AddressType.CURRENT
                    );

            return;
        }

        /*
         * Permanent address is not the current address.
         */
        permanentAddress.setCurrent(false);

        addressRepository.save(
                permanentAddress
        );

        AddressRequest currentRequest =
                request.getCurrentAddress();

        /*
         * If current address is not supplied, remove an old
         * separate current-address row.
         */
        if (currentRequest == null) {

            addressRepository
                    .deleteByUserIdAndAddressType(
                            user.getId(),
                            AddressType.CURRENT
                    );

            return;
        }

        /*
         * Create or update separate current address.
         */
        UserAddress currentAddress =
                getOrCreateAddress(
                        user,
                        AddressType.CURRENT
                );

        copyAddress(
                currentRequest,
                currentAddress
        );

        currentAddress.setCurrent(true);

        addressRepository.save(
                currentAddress
        );
    }

    /**
     * Finds an existing address by type or creates a new one.
     */
    private UserAddress getOrCreateAddress(
            User user,
            AddressType addressType
    ) {

        return addressRepository
                .findFirstByUserIdAndAddressType(
                        user.getId(),
                        addressType
                )
                .orElseGet(() -> {

                    UserAddress address =
                            new UserAddress();

                    address.setUser(user);

                    address.setAddressType(
                            addressType
                    );

                    return address;
                });
    }

    /**
     * Copies address request values into an address entity.
     */
    private void copyAddress(
            AddressRequest source,
            UserAddress target
    ) {

        target.setAddressLine1(
                requireTrimmed(
                        source.getAddressLine1(),
                        "Address line 1 is required"
                )
        );

        target.setAddressLine2(
                trimToNull(
                        source.getAddressLine2()
                )
        );

        target.setAddressLine3(
                trimToNull(
                        source.getAddressLine3()
                )
        );

        target.setArea(
                trimToNull(
                        source.getArea()
                )
        );

        target.setCity(
                requireTrimmed(
                        source.getCity(),
                        "City is required"
                )
        );

        target.setState(
                requireTrimmed(
                        source.getState(),
                        "State is required"
                )
        );

        target.setZipCode(
                trimToNull(
                        source.getZipCode()
                )
        );
    }

    /**
     * Calculates whether all important profile information
     * has been completed.
     */
    private boolean calculateProfileCompletion(
            User user,
            LocalGuideProfile profile,
            UpdateGuideProfileRequest request
    ) {

        boolean commonDetailsComplete =
                hasText(user.getFirstName())
                        && hasText(user.getLastName())
                        && hasText(user.getPhoneNumber())
                        && user.getGender() != null
                        && user.getDateOfBirth() != null;

        boolean guideDetailsComplete =
                hasText(profile.getOccupation())
                        && profile.getExperienceYears() != null
                        && profile.getExperienceYears() >= 0
                        && hasText(profile.getExpertise())
                        && hasText(
                                profile.getLanguagesSpoken()
                        )
                        && hasText(profile.getAboutMe());

        boolean permanentAddressComplete =
                isAddressComplete(
                        request.getPermanentAddress()
                );

        boolean currentAddressComplete =
                request.isCurrentAddressSameAsPermanent()
                        || isAddressComplete(
                                request.getCurrentAddress()
                        );

        return commonDetailsComplete
                && guideDetailsComplete
                && permanentAddressComplete
                && currentAddressComplete;
    }

    private boolean isAddressComplete(
            AddressRequest address
    ) {

        return address != null
                && hasText(
                        address.getAddressLine1()
                )
                && hasText(address.getCity())
                && hasText(address.getState());
    }

    /**
     * Converts profile entities into response DTO.
     */
    private LocalGuideProfileResponse convertToResponse(
            User user,
            LocalGuideProfile profile,
            boolean privateProfile
    ) {

        List<AddressResponse> addresses;

        if (privateProfile) {

            addresses = getGuideAddresses(
                    user.getId()
            );

        } else {

            addresses = Collections.emptyList();
        }

        List<PlaceResponse> places =
                placeService.getGuidePlaces(
                        user.getId()
                );

        UserDetailInfo detailInfo =
                detailInfoRepository
                        .findByUserId(
                                user.getId()
                        )
                        .orElse(null);

        String profileImageUrl =
                photoRepository
                        .findByUserIdAndProfilePhotoTrue(
                                user.getId()
                        )
                        .map(UserPhoto::getPhotoUrl)
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

        boolean sameAsPermanent =
                addresses.stream()
                        .anyMatch(address ->
                                AddressType.PERMANENT
                                        .name()
                                        .equals(
                                                address
                                                        .getAddressType()
                                        )
                                        && address.isCurrent()
                        );

        return LocalGuideProfileResponse
                .builder()

                .profileId(
                        profile == null
                                ? null
                                : profile.getId()
                )

                .userId(user.getId())

                .firstName(
                        user.getFirstName()
                )

                .lastName(
                        user.getLastName()
                )

                .fullName(fullName)

                .email(
                        privateProfile
                                ? user.getEmail()
                                : null
                )

                .phoneNumber(
                        privateProfile
                                ? user.getPhoneNumber()
                                : null
                )

                .gender(
                        privateProfile
                                && user.getGender() != null
                                ? user.getGender().name()
                                : null
                )

                .dateOfBirth(
                        privateProfile
                                ? user.getDateOfBirth()
                                : null
                )

                .profileImageUrl(
                        profileImageUrl
                )

                .aboutMe(
                        profile == null
                                ? null
                                : profile.getAboutMe()
                )

                .experienceYears(
                        profile == null
                                ? null
                                : profile
                                        .getExperienceYears()
                )

                .occupation(
                        profile == null
                                ? null
                                : profile.getOccupation()
                )

                .expertise(
                        profile == null
                                ? null
                                : profile.getExpertise()
                )

                .languagesSpoken(
                        profile == null
                                ? null
                                : profile
                                        .getLanguagesSpoken()
                )

                .websiteUrl(
                        profile == null
                                ? null
                                : profile.getWebsiteUrl()
                )

                .instagramProfile(
                        detailInfo == null
                                ? null
                                : detailInfo
                                        .getInstagramProfile()
                )

                .facebookProfile(
                        detailInfo == null
                                ? null
                                : detailInfo
                                        .getFacebookProfile()
                )

                .verificationDocumentUrl(
                        privateProfile
                                && profile != null
                                ? profile
                                        .getVerificationDocumentUrl()
                                : null
                )

                .verificationStatus(
                        profile == null
                                || profile
                                        .getVerificationStatus()
                                        == null
                                ? null
                                : profile
                                        .getVerificationStatus()
                                        .name()
                )

                .profileCompleted(
                        profile != null
                                && profile
                                        .isProfileCompleted()
                )

                .currentAddressSameAsPermanent(
                        sameAsPermanent
                )

                .addresses(addresses)

                .places(places)

                .build();
    }

    /**
     * Converts UserAddress entity to AddressResponse DTO.
     */
    private AddressResponse convertAddress(
            UserAddress address
    ) {

        return AddressResponse
                .builder()

                .id(address.getId())

                .addressLine1(
                        address.getAddressLine1()
                )

                .addressLine2(
                        address.getAddressLine2()
                )

                .addressLine3(
                        address.getAddressLine3()
                )

                .area(address.getArea())

                .city(address.getCity())

                .state(address.getState())

                .zipCode(
                        address.getZipCode()
                )

                .addressType(
                        address.getAddressType() == null
                                ? null
                                : address
                                        .getAddressType()
                                        .name()
                )

                .current(
                        address.isCurrent()
                )

                .build();
    }

    /**
     * Finds and validates a local-guide user.
     */
    private User getGuide(
            Long userId
    ) {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "Guide user ID cannot be null"
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

        if (user.getRole()
                != UserRole.LOCAL_GUIDE) {

            throw new IllegalArgumentException(
                    "User is not registered as a local guide"
            );
        }

        if (!user.isActive()) {
            throw new IllegalArgumentException(
                    "Local guide account is inactive"
            );
        }

        return user;
    }

    /**
     * Loads permanent and current addresses without requiring
     * a custom OrderByCurrent repository method.
     */
    private List<AddressResponse> getGuideAddresses(
            Long userId
    ) {

        UserAddress permanentAddress =
                addressRepository
                        .findFirstByUserIdAndAddressType(
                                userId,
                                AddressType.PERMANENT
                        )
                        .orElse(null);

        UserAddress currentAddress =
                addressRepository
                        .findFirstByUserIdAndAddressType(
                                userId,
                                AddressType.CURRENT
                        )
                        .orElse(null);

        if (permanentAddress == null
                && currentAddress == null) {

            return Collections.emptyList();
        }

        java.util.ArrayList<AddressResponse> addresses =
                new java.util.ArrayList<>();

        /*
         * Add the current address first. If the permanent
         * address itself is current, it is added first.
         */
        if (permanentAddress != null
                && permanentAddress.isCurrent()) {

            addresses.add(
                    convertAddress(
                            permanentAddress
                    )
            );
        }

        if (currentAddress != null) {

            addresses.add(
                    convertAddress(
                            currentAddress
                    )
            );
        }

        if (permanentAddress != null
                && !permanentAddress.isCurrent()) {

            addresses.add(
                    convertAddress(
                            permanentAddress
                    )
            );
        }

        return addresses;
    }

    /**
     * Converts the gender received from the request into the
     * GenderType enum stored by User.
     */
    private GenderType parseGender(
            String value
    ) {

        String gender =
                trimToNull(value);

        if (gender == null) {
            return null;
        }

        try {
            return GenderType.valueOf(
                    gender
                            .trim()
                            .toUpperCase()
                            .replace(' ', '_')
                            .replace('-', '_')
            );
        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException(
                    "Invalid gender value: "
                            + value
            );
        }
    }

    private String normalizePhone(
            String value
    ) {

        if (value == null) {
            return null;
        }

        return value.replaceAll(
                "[^0-9+]",
                ""
        );
    }

    private String requireTrimmed(
            String value,
            String errorMessage
    ) {

        String result =
                trimToNull(value);

        if (result == null) {
            throw new IllegalArgumentException(
                    errorMessage
            );
        }

        return result;
    }

    private String trimToNull(
            String value
    ) {

        if (value == null
                || value.isBlank()) {

            return null;
        }

        return value.trim();
    }

    private String trimToEmpty(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private boolean hasText(
            String value
    ) {

        return value != null
                && !value.isBlank();
    }
}