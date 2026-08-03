package com.locallens.dto.traveller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
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

    @Builder.Default
    private List<String> interests = new ArrayList<>();

    @Size(
        max = 50,
        message = "Travel style cannot exceed 50 characters"
    )
    private String travelStyle;

    @Size(
        max = 50,
        message = "Preferred travel type cannot exceed 50 characters"
    )
    private String preferredTravelType;

    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Minimum budget cannot be negative"
    )
    private BigDecimal minimumBudget;

    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Maximum budget cannot be negative"
    )
    private BigDecimal maximumBudget;

    @Size(
        max = 50,
        message = "Budget preference cannot exceed 50 characters"
    )
    private String budgetPreference;

    @Builder.Default
    private List<String> preferredLanguages = new ArrayList<>();

    @Builder.Default
    private List<String> dietaryPreferences = new ArrayList<>();

    @Builder.Default
    private List<String> accessibilityRequirements = new ArrayList<>();

    @Builder.Default
    private List<String> preferredCategories = new ArrayList<>();

    private Boolean prefersHiddenGems;

    private Boolean prefersLocalGuides;

    private Boolean prefersFamilyFriendlyPlaces;
}