package com.locallens.dto.traveller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
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
public class TravellerPreferenceRequest {

    /*
     * Examples:
     * Solo, Couple, Family, Friends, Business, Adventure
     */
    @Builder.Default
    private List<String> travelStyles =
            new ArrayList<>();

    /*
     * Examples:
     * Budget, Standard, Luxury
     */
    @Builder.Default
    private List<String> budgetPreference =
            new ArrayList<>();

    /*
     * Examples:
     * Bike, Car, Train, Bus, Flight, Walking
     */
    @Builder.Default
    private List<String> preferredTransportation =
            new ArrayList<>();

    /*
     * Examples:
     * Hotel, Hostel, Resort, Camping, Homestay
     */
    @Builder.Default
    private List<String> preferredAccommodations =
            new ArrayList<>();

    /*
     * Examples:
     * Nature, Beach, Adventure, Historical, Food
     */
    @Builder.Default
    private List<String> favouriteCategories =
            new ArrayList<>();

    /*
     * Preferred languages while travelling.
     */
    @Builder.Default
    private List<String> preferredLanguages =
            new ArrayList<>();

    /*
     * Examples:
     * Weekend, 3 Days, 5 Days, 1 Week, Custom
     */
    @Builder.Default
    private List<String> preferredTripDurations =
            new ArrayList<>();

    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Maximum daily distance cannot be negative"
    )
    private BigDecimal maxDailyDistance;

    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Maximum budget cannot be negative"
    )
    private BigDecimal maxBudget;

    /*
     * Optional additional preferences.
     */

    @Builder.Default
    private List<String> dietaryPreferences =
            new ArrayList<>();

    @Builder.Default
    private List<String> accessibilityRequirements =
            new ArrayList<>();

    private Boolean prefersHiddenGems;

    private Boolean prefersLocalGuides;

    private Boolean prefersFamilyFriendlyPlaces;
}