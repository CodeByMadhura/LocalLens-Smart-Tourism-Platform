package com.locallens.dto.traveller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
public class TravellerPreferenceResponse {

    private Long id;

    @Builder.Default
    private List<String> interests = new ArrayList<>();

    private String travelStyle;

    private String preferredTravelType;

    private BigDecimal minimumBudget;

    private BigDecimal maximumBudget;

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