package com.locallens.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "traveller_profiles")
public class TravellerProfile extends BaseEntity {

    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true
    )
    private User user;

    @Column(
        name = "bio",
        columnDefinition = "TEXT"
    )
    private String bio;

    @Column(
        name = "occupation",
        length = 150
    )
    private String occupation;

    /*
     * Multiple frontend travel-style selections are temporarily
     * stored as comma-separated text.
     */
    @Column(
        name = "travel_style",
        length = 500
    )
    private String travelStyle;

    /*
     * Multiple preferred languages may temporarily be stored
     * as comma-separated text.
     */
    @Column(
        name = "preferred_language",
        length = 500
    )
    private String preferredLanguage;

    @Column(
        name = "home_city",
        length = 100
    )
    private String homeCity;

    @Column(
        name = "home_state",
        length = 100
    )
    private String homeState;

    @Column(
        name = "country",
        length = 100
    )
    private String country;

    /*
     * This numeric field should store the maximum budget sent by
     * the traveller profile page.
     */
    @Column(
        name = "preferred_budget",
        precision = 12,
        scale = 2
    )
    private BigDecimal preferredBudget;

    /*
     * Transportation, accommodation and trip-duration values can
     * temporarily be stored as comma-separated text.
     */
    @Column(
        name = "preferred_travel_type",
        length = 1000
    )
    private String preferredTravelType;

    @Column(
        name = "emergency_contact_name",
        length = 150
    )
    private String emergencyContactName;

    @Column(
        name = "emergency_contact_number",
        length = 20
    )
    private String emergencyContactNumber;

    @Column(
        name = "profile_image_url",
        length = 500
    )
    private String profileImageUrl;

    /*
     * Favourite categories/interests.
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "traveller_interests",
        joinColumns = @JoinColumn(
            name = "traveller_profile_id"
        )
    )
    @Column(
        name = "interest",
        nullable = false,
        length = 100
    )
    private List<String> interests =
            new ArrayList<>();

    /*
     * Languages spoken by the traveller.
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "traveller_languages",
        joinColumns = @JoinColumn(
            name = "traveller_profile_id"
        )
    )
    @Column(
        name = "language",
        nullable = false,
        length = 100
    )
    private List<String> languagesSpoken =
            new ArrayList<>();

    /*
     * Accessibility preferences.
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "traveller_accessibility_preferences",
        joinColumns = @JoinColumn(
            name = "traveller_profile_id"
        )
    )
    @Column(
        name = "preference",
        nullable = false,
        length = 150
    )
    private List<String> accessibilityPreferences =
            new ArrayList<>();
}