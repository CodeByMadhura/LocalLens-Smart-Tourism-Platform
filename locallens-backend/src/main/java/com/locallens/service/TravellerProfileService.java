package com.locallens.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.locallens.dto.traveller.TravellerAddressRequest;
import com.locallens.dto.traveller.TravellerAddressResponse;
import com.locallens.dto.traveller.TravellerPreferenceRequest;
import com.locallens.dto.traveller.TravellerProfileResponse;
import com.locallens.dto.traveller.UpdateTravellerProfileRequest;
import com.locallens.entities.TravellerProfile;
import com.locallens.entities.User;
import com.locallens.entities.UserAddress;
import com.locallens.enums.AddressType;
import com.locallens.enums.UserRole;
import com.locallens.repository.TravellerProfileRepository;
import com.locallens.repository.UserAddressRepository;
import com.locallens.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravellerProfileService {

    private final UserRepository userRepository;

    private final TravellerProfileRepository travellerProfileRepository;

    private final UserAddressRepository userAddressRepository;

    /**
     * Load the currently logged-in traveller's profile.
     */
    @Transactional
    public TravellerProfileResponse getProfileByEmail(
            String email
    ) {
        User user = getTravellerByEmail(email);

        TravellerProfile profile =
                findOrCreateProfile(user);

        UserAddress permanentAddress =
                findAddress(
                        user.getId(),
                        AddressType.PERMANENT
                );

        UserAddress currentAddress =
                findAddress(
                        user.getId(),
                        AddressType.CURRENT
                );

        return mapToResponse(
                user,
                profile,
                permanentAddress,
                currentAddress
        );
    }

    /**
     * Update personal information, traveller preferences,
     * permanent address and current address.
     */
    @Transactional
    public TravellerProfileResponse updateProfileByEmail(
            String email,
            UpdateTravellerProfileRequest request
    ) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "Traveller profile request is required"
            );
        }

        User user = getTravellerByEmail(email);

        TravellerProfile profile =
                findOrCreateProfile(user);

        updateUserFields(
                user,
                request
        );

        updateTravellerProfileFields(
                profile,
                request
        );

        TravellerAddressRequest permanentAddressRequest =
                request.getPermanentAddress();

        TravellerAddressRequest currentAddressRequest =
                request.getCurrentAddress();

        UserAddress permanentAddress;
        UserAddress currentAddress;

        /*
         * Save or update permanent address.
         */
        if (hasAddressInformation(permanentAddressRequest)) {
            permanentAddress = saveOrUpdateAddress(
                    user,
                    permanentAddressRequest,
                    AddressType.PERMANENT,
                    false
            );
        } else {
            permanentAddress = findAddress(
                    user.getId(),
                    AddressType.PERMANENT
            );
        }

        boolean sameAddress =
                Boolean.TRUE.equals(
                        request.getSameAddress()
                );

        /*
         * Save or update current address.
         *
         * When both addresses are the same, copy the permanent
         * address values into a separate CURRENT row.
         */
        if (sameAddress
                && hasAddressInformation(
                        permanentAddressRequest
                )) {

            currentAddress = saveOrUpdateAddress(
                    user,
                    permanentAddressRequest,
                    AddressType.CURRENT,
                    true
            );

        } else if (hasAddressInformation(
                currentAddressRequest
        )) {

            currentAddress = saveOrUpdateAddress(
                    user,
                    currentAddressRequest,
                    AddressType.CURRENT,
                    true
            );

        } else {
            currentAddress = findAddress(
                    user.getId(),
                    AddressType.CURRENT
            );
        }

        boolean profileCompleted =
                calculateProfileCompleted(
                        user,
                        profile,
                        permanentAddress
                );

        user.setProfileCompleted(
                profileCompleted
        );

        User savedUser =
                userRepository.save(user);

        TravellerProfile savedProfile =
                travellerProfileRepository.save(profile);

        return mapToResponse(
                savedUser,
                savedProfile,
                permanentAddress,
                currentAddress
        );
    }

    /**
     * Update only the traveller profile-image URL.
     */
    @Transactional
    public TravellerProfileResponse updateProfileImage(
            String email,
            String imageUrl
    ) {
        User user = getTravellerByEmail(email);

        TravellerProfile profile =
                findOrCreateProfile(user);

        profile.setProfileImageUrl(
                clean(imageUrl)
        );

        TravellerProfile savedProfile =
                travellerProfileRepository.save(profile);

        UserAddress permanentAddress =
                findAddress(
                        user.getId(),
                        AddressType.PERMANENT
                );

        UserAddress currentAddress =
                findAddress(
                        user.getId(),
                        AddressType.CURRENT
                );

        return mapToResponse(
                user,
                savedProfile,
                permanentAddress,
                currentAddress
        );
    }

    /**
     * Find the authenticated traveller.
     */
    private User getTravellerByEmail(
            String email
    ) {
        if (!hasText(email)) {
            throw new IllegalArgumentException(
                    "Authenticated user email is required"
            );
        }

        User user = userRepository
                .findByEmailIgnoreCase(
                        email.trim()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Traveller user was not found"
                        )
                );

        if (!UserRole.TRAVELLER.equals(
                user.getRole()
        )) {
            throw new IllegalArgumentException(
                    "Only travellers can access the traveller profile"
            );
        }

        return user;
    }

    /**
     * Return the existing profile or create a blank profile.
     */
    private TravellerProfile findOrCreateProfile(
            User user
    ) {
        return travellerProfileRepository
                .findByUserId(user.getId())
                .orElseGet(() -> {
                    TravellerProfile profile =
                            new TravellerProfile();

                    profile.setUser(user);

                    profile.setInterests(
                            new ArrayList<>()
                    );

                    profile.setLanguagesSpoken(
                            new ArrayList<>()
                    );

                    profile.setAccessibilityPreferences(
                            new ArrayList<>()
                    );

                    return travellerProfileRepository
                            .save(profile);
                });
    }

    /**
     * Update fields belonging to the User entity.
     */
    private void updateUserFields(
            User user,
            UpdateTravellerProfileRequest request
    ) {
        if (request.getFirstName() != null) {
            user.setFirstName(
                    clean(request.getFirstName())
            );
        }

        if (request.getLastName() != null) {
            user.setLastName(
                    clean(request.getLastName())
            );
        }

        if (request.getPhoneNumber() != null) {
            String newPhoneNumber =
                    clean(request.getPhoneNumber());

            String existingPhoneNumber =
                    clean(user.getPhoneNumber());

            boolean phoneChanged =
                    !Objects.equals(
                            existingPhoneNumber,
                            newPhoneNumber
                    );

            user.setPhoneNumber(
                    newPhoneNumber
            );

            if (phoneChanged) {
                user.setPhoneVerified(false);
            }
        }

        if (request.getGender() != null) {
            user.setGender(
                    request.getGender()
            );
        }

        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(
                    request.getDateOfBirth()
            );
        }
    }

    /**
     * Update fields belonging to TravellerProfile.
     */
    private void updateTravellerProfileFields(
            TravellerProfile profile,
            UpdateTravellerProfileRequest request
    ) {
        /*
         * The frontend sends "aboutMe", while the database
         * entity stores it in the "bio" column.
         */
        if (request.getAboutMe() != null) {
            profile.setBio(
                    clean(request.getAboutMe())
            );
        }

        if (request.getOccupation() != null) {
            profile.setOccupation(
                    clean(request.getOccupation())
            );
        }

        if (request.getLanguagesSpoken() != null) {
            setLanguagesSpoken(
                    profile,
                    request.getLanguagesSpoken()
            );
        }

        if (request.getAccessibilityPreferences() != null) {
            setAccessibilityPreferences(
                    profile,
                    request.getAccessibilityPreferences()
            );
        }

        if (request.getEmergencyContactName() != null) {
            profile.setEmergencyContactName(
                    clean(
                            request.getEmergencyContactName()
                    )
            );
        }

        if (request.getEmergencyContactNumber() != null) {
            profile.setEmergencyContactNumber(
                    clean(
                            request.getEmergencyContactNumber()
                    )
            );
        }

        if (request.getProfileImageUrl() != null) {
            profile.setProfileImageUrl(
                    clean(request.getProfileImageUrl())
            );
        }

        TravellerPreferenceRequest preferences =
                request.getTravelPreferences();

        if (preferences == null) {
            return;
        }

        /*
         * Store multiple selected travel styles as
         * comma-separated text.
         */
        if (preferences.getTravelStyles() != null) {
            profile.setTravelStyle(
                    joinValues(
                            preferences.getTravelStyles()
                    )
            );
        }

        /*
         * IMPORTANT:
         *
         * preferredBudget is BigDecimal in TravellerProfile.
         * Therefore store maxBudget here.
         *
         * Do not call joinValues(getBudgetPreference()) because
         * joinValues returns String.
         */
        if (preferences.getMaxBudget() != null) {
            profile.setPreferredBudget(
                    preferences.getMaxBudget()
            );
        }

        /*
         * Store preferred languages as comma-separated text.
         */
        if (preferences.getPreferredLanguages() != null) {
            profile.setPreferredLanguage(
                    joinValues(
                            preferences.getPreferredLanguages()
                    )
            );
        }

        /*
         * Store favourite categories in traveller_interests.
         */
        if (preferences.getFavouriteCategories() != null) {
            setInterests(
                    profile,
                    preferences.getFavouriteCategories()
            );
        }

        /*
         * The current entity has only one String field named
         * preferredTravelType. Until separate columns/tables are
         * added, combine transportation, accommodations and
         * trip durations in that field.
         */
        List<String> travelTypes =
                new ArrayList<>();

        addAllValues(
                travelTypes,
                preferences.getPreferredTransportation()
        );

        addAllValues(
                travelTypes,
                preferences.getPreferredAccommodations()
        );

        addAllValues(
                travelTypes,
                preferences.getPreferredTripDurations()
        );

        if (!travelTypes.isEmpty()) {
            profile.setPreferredTravelType(
                    joinValues(travelTypes)
            );
        } else {
            profile.setPreferredTravelType(null);
        }

        /*
         * budgetPreference contains text labels such as:
         *
         * Budget
         * Standard
         * Luxury
         *
         * It is intentionally not assigned to preferredBudget,
         * because preferredBudget is a BigDecimal column.
         *
         * To save these labels permanently, add a separate
         * String field or an ElementCollection to TravellerProfile.
         */
    }

    /**
     * Replace spoken languages.
     */
    private void setLanguagesSpoken(
            TravellerProfile profile,
            List<String> languages
    ) {
        if (profile.getLanguagesSpoken() == null) {
            profile.setLanguagesSpoken(
                    new ArrayList<>()
            );
        }

        replaceList(
                profile.getLanguagesSpoken(),
                languages
        );
    }

    /**
     * Replace traveller interests/categories.
     */
    private void setInterests(
            TravellerProfile profile,
            List<String> interests
    ) {
        if (profile.getInterests() == null) {
            profile.setInterests(
                    new ArrayList<>()
            );
        }

        replaceList(
                profile.getInterests(),
                interests
        );
    }

    /**
     * Replace accessibility preferences.
     */
    private void setAccessibilityPreferences(
            TravellerProfile profile,
            List<String> preferences
    ) {
        if (profile.getAccessibilityPreferences() == null) {
            profile.setAccessibilityPreferences(
                    new ArrayList<>()
            );
        }

        replaceList(
                profile.getAccessibilityPreferences(),
                preferences
        );
    }

    /**
     * Save or update one address type for the traveller.
     */
    private UserAddress saveOrUpdateAddress(
            User user,
            TravellerAddressRequest request,
            AddressType addressType,
            boolean current
    ) {
        if (request == null) {
            return null;
        }

        validateAddressForSave(
                request,
                addressType
        );

        UserAddress address =
                userAddressRepository
                        .findFirstByUserIdAndAddressType(
                                user.getId(),
                                addressType
                        )
                        .orElseGet(UserAddress::new);

        address.setUser(user);

        address.setAddressType(
                addressType
        );

        address.setAddressLine1(
                clean(request.getAddressLine1())
        );

        address.setAddressLine2(
                clean(request.getAddressLine2())
        );

        address.setAddressLine3(
                clean(request.getAddressLine3())
        );

        address.setArea(
                clean(request.getArea())
        );

        address.setCity(
                clean(request.getCity())
        );

        address.setState(
                clean(request.getState())
        );

        address.setZipCode(
                clean(request.getZipCode())
        );

        /*
         * PERMANENT -> false
         * CURRENT   -> true
         */
        address.setCurrent(current);

        return userAddressRepository.save(address);
    }

    /**
     * Validate mandatory database address fields before saving.
     */
    private void validateAddressForSave(
            TravellerAddressRequest request,
            AddressType addressType
    ) {
        if (!hasText(request.getAddressLine1())) {
            throw new IllegalArgumentException(
                    addressType
                            + " address line 1 is required"
            );
        }

        if (!hasText(request.getCity())) {
            throw new IllegalArgumentException(
                    addressType
                            + " address city is required"
            );
        }

        if (!hasText(request.getState())) {
            throw new IllegalArgumentException(
                    addressType
                            + " address state is required"
            );
        }
    }

    /**
     * Find an address by user and address type.
     */
    private UserAddress findAddress(
            Long userId,
            AddressType addressType
    ) {
        return userAddressRepository
                .findFirstByUserIdAndAddressType(
                        userId,
                        addressType
                )
                .orElse(null);
    }

    /**
     * Check whether any address field was entered.
     */
    private boolean hasAddressInformation(
            TravellerAddressRequest request
    ) {
        if (request == null) {
            return false;
        }

        return hasText(request.getAddressLine1())
                || hasText(request.getAddressLine2())
                || hasText(request.getAddressLine3())
                || hasText(request.getArea())
                || hasText(request.getCity())
                || hasText(request.getState())
                || hasText(request.getZipCode());
    }

    /**
     * Convert entities into the traveller profile response.
     */
    private TravellerProfileResponse mapToResponse(
            User user,
            TravellerProfile profile,
            UserAddress permanentAddress,
            UserAddress currentAddress
    ) {
        TravellerPreferenceRequest preferencesResponse =
                buildPreferenceResponse(profile);

        TravellerAddressResponse permanentAddressResponse =
                mapAddress(permanentAddress);

        TravellerAddressResponse currentAddressResponse =
                mapAddress(currentAddress);

        TravellerAddressResponse legacyAddressResponse =
                currentAddressResponse != null
                        ? currentAddressResponse
                        : permanentAddressResponse;

        return TravellerProfileResponse.builder()
                .userId(user.getId())
                .travellerProfileId(profile.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .active(user.isActive())
                .emailVerified(user.isEmailVerified())
                .phoneVerified(user.isPhoneVerified())
                .profileCompleted(
                        user.isProfileCompleted()
                )
                .verificationStatus(
                        user.getVerificationStatus()
                )

                /*
                 * Return both names for frontend compatibility.
                 */
                .aboutMe(profile.getBio())
                .bio(profile.getBio())

                .occupation(profile.getOccupation())
                .travelStyle(profile.getTravelStyle())
                .preferredLanguage(
                        profile.getPreferredLanguage()
                )
                .homeCity(profile.getHomeCity())
                .homeState(profile.getHomeState())
                .country(profile.getCountry())
                .preferredBudget(
                        profile.getPreferredBudget()
                )
                .preferredTravelType(
                        profile.getPreferredTravelType()
                )
                .emergencyContactName(
                        profile.getEmergencyContactName()
                )
                .emergencyContactNumber(
                        profile.getEmergencyContactNumber()
                )
                .profileImageUrl(
                        profile.getProfileImageUrl()
                )
                .interests(
                        copyList(profile.getInterests())
                )
                .languagesSpoken(
                        copyList(
                                profile.getLanguagesSpoken()
                        )
                )
                .accessibilityPreferences(
                        copyList(
                                profile.getAccessibilityPreferences()
                        )
                )
                .permanentAddress(
                        permanentAddressResponse
                )
                .currentAddress(
                        currentAddressResponse
                )
                .address(
                        legacyAddressResponse
                )
                .sameAddress(
                        addressesAreEqual(
                                permanentAddress,
                                currentAddress
                        )
                )
                .travelPreferences(
                        preferencesResponse
                )
                .build();
    }

    /**
     * Build travel preferences for the frontend response.
     */
    private TravellerPreferenceRequest buildPreferenceResponse(
            TravellerProfile profile
    ) {
        List<String> combinedTravelTypes =
                splitValues(
                        profile.getPreferredTravelType()
                );

        return TravellerPreferenceRequest.builder()
                .travelStyles(
                        splitValues(
                                profile.getTravelStyle()
                        )
                )

                /*
                 * The text labels Budget/Standard/Luxury are not
                 * currently stored in the entity.
                 */
                .budgetPreference(
                        new ArrayList<>()
                )

                /*
                 * Because transportation, accommodation and duration
                 * currently share one database field, the same list is
                 * returned for all three until separate storage exists.
                 */
                .preferredTransportation(
                        new ArrayList<>(
                                combinedTravelTypes
                        )
                )
                .preferredAccommodations(
                        new ArrayList<>(
                                combinedTravelTypes
                        )
                )
                .preferredTripDurations(
                        new ArrayList<>(
                                combinedTravelTypes
                        )
                )
                .favouriteCategories(
                        copyList(
                                profile.getInterests()
                        )
                )
                .preferredLanguages(
                        splitValues(
                                profile.getPreferredLanguage()
                        )
                )
                .maxDailyDistance(null)
                .maxBudget(
                        profile.getPreferredBudget()
                )
                .build();
    }

    /**
     * Convert UserAddress to TravellerAddressResponse.
     */
    private TravellerAddressResponse mapAddress(
            UserAddress address
    ) {
        if (address == null) {
            return null;
        }

        return TravellerAddressResponse.builder()
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
                .zipCode(address.getZipCode())
                .addressType(
                        address.getAddressType()
                )
                .current(address.isCurrent())
                .build();
    }

    /**
     * Check whether permanent and current addresses contain
     * identical values.
     */
    private boolean addressesAreEqual(
            UserAddress permanentAddress,
            UserAddress currentAddress
    ) {
        if (permanentAddress == null
                || currentAddress == null) {
            return false;
        }

        return Objects.equals(
                        clean(
                                permanentAddress
                                        .getAddressLine1()
                        ),
                        clean(
                                currentAddress
                                        .getAddressLine1()
                        )
                )
                && Objects.equals(
                        clean(
                                permanentAddress
                                        .getAddressLine2()
                        ),
                        clean(
                                currentAddress
                                        .getAddressLine2()
                        )
                )
                && Objects.equals(
                        clean(
                                permanentAddress
                                        .getAddressLine3()
                        ),
                        clean(
                                currentAddress
                                        .getAddressLine3()
                        )
                )
                && Objects.equals(
                        clean(permanentAddress.getArea()),
                        clean(currentAddress.getArea())
                )
                && Objects.equals(
                        clean(permanentAddress.getCity()),
                        clean(currentAddress.getCity())
                )
                && Objects.equals(
                        clean(permanentAddress.getState()),
                        clean(currentAddress.getState())
                )
                && Objects.equals(
                        clean(
                                permanentAddress.getZipCode()
                        ),
                        clean(
                                currentAddress.getZipCode()
                        )
                );
    }

    /**
     * Decide whether the profile is complete.
     */
    private boolean calculateProfileCompleted(
            User user,
            TravellerProfile profile,
            UserAddress permanentAddress
    ) {
        return hasText(user.getFirstName())
                && hasText(user.getLastName())
                && hasText(user.getPhoneNumber())
                && user.getGender() != null
                && user.getDateOfBirth() != null
                && hasText(profile.getBio())
                && hasText(profile.getOccupation())
                && permanentAddress != null
                && hasText(
                        permanentAddress.getAddressLine1()
                )
                && hasText(
                        permanentAddress.getCity()
                )
                && hasText(
                        permanentAddress.getState()
                );
    }

    /**
     * Replace a destination collection with cleaned values.
     */
    private void replaceList(
            List<String> destination,
            List<String> source
    ) {
        if (destination == null
                || source == null) {
            return;
        }

        destination.clear();

        source.stream()
                .filter(this::hasText)
                .map(String::trim)
                .distinct()
                .forEach(destination::add);
    }

    /**
     * Add cleaned source values into another list.
     */
    private void addAllValues(
            List<String> destination,
            List<String> source
    ) {
        if (source == null) {
            return;
        }

        source.stream()
                .filter(this::hasText)
                .map(String::trim)
                .distinct()
                .forEach(destination::add);
    }

    /**
     * Convert a list into comma-separated text.
     */
    private String joinValues(
            List<String> values
    ) {
        if (values == null) {
            return null;
        }

        return values.stream()
                .filter(this::hasText)
                .map(String::trim)
                .distinct()
                .reduce(
                        (first, second) ->
                                first + ", " + second
                )
                .orElse(null);
    }

    /**
     * Convert comma-separated text back into a list.
     */
    private List<String> splitValues(
            String value
    ) {
        if (!hasText(value)) {
            return new ArrayList<>();
        }

        return Arrays.stream(
                        value.split(",")
                )
                .map(String::trim)
                .filter(this::hasText)
                .distinct()
                .toList();
    }

    private List<String> copyList(
            List<String> source
    ) {
        return source == null
                ? new ArrayList<>()
                : new ArrayList<>(source);
    }

    private boolean hasText(
            String value
    ) {
        return value != null
                && !value.trim().isEmpty();
    }

    private String clean(
            String value
    ) {
        return value == null
                ? null
                : value.trim();
    }
}