package com.locallens.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.locallens.enums.TravelType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TripPlannerRequest {

    /*
     * Main city entered or selected by the traveller.
     *
     * The frontend currently sends both:
     * city
     * destination
     *
     * Therefore, both fields are supported.
     */
    @Size(
        max = 150,
        message = "City cannot exceed 150 characters."
    )
    private String city;

    @NotBlank(
        message = "Destination city is required."
    )
    @Size(
        max = 150,
        message = "Destination cannot exceed 150 characters."
    )
    private String destination;

    @NotNull(
        message = "Start date is required."
    )
    @FutureOrPresent(
        message = "Start date cannot be in the past."
    )
    private LocalDate startDate;

    @NotNull(
        message = "Number of days is required."
    )
    @Min(
        value = 1,
        message = "Number of days must be at least 1."
    )
    @Max(
        value = 30,
        message = "Number of days cannot exceed 30."
    )
    private Integer numberOfDays;

    /*
     * Main field used by the updated frontend.
     */
    @Min(
        value = 1,
        message = "Number of persons must be at least 1."
    )
    @Max(
        value = 50,
        message = "Number of persons cannot exceed 50."
    )
    private Integer numberOfPersons;

    /*
     * Compatibility field for older frontend code.
     */
    @Min(
        value = 1,
        message = "Number of travellers must be at least 1."
    )
    @Max(
        value = 50,
        message = "Number of travellers cannot exceed 50."
    )
    private Integer travellers;

    /*
     * Main budget field used by the updated frontend.
     */
    @DecimalMin(
        value = "0.01",
        message = "Total budget must be greater than zero."
    )
    private BigDecimal totalBudget;

    /*
     * Compatibility field for older frontend code.
     */
    @DecimalMin(
        value = "0.01",
        message = "Budget must be greater than zero."
    )
    private BigDecimal budget;

    @Min(
        value = 1,
        message = "Nearby radius must be at least 1 kilometre."
    )
    @Max(
        value = 500,
        message = "Nearby radius cannot exceed 500 kilometres."
    )
    private Integer nearbyRadiusKm = 25;

    private TravelType travelType = TravelType.SOLO;

    /*
     * Compatibility field if any older code uses travelStyle.
     */
    private TravelType travelStyle;

    private List<String> preferredCategories =
            new ArrayList<>();

    /*
     * Compatibility field for older frontend code.
     */
    private List<String> categories =
            new ArrayList<>();

    /*
     * IDs of approved places matching the selected city.
     */
    private List<Long> placeIds =
            new ArrayList<>();

    private Long selectedPlaceId;

    public TripPlannerRequest() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(
            String city
    ) {
        this.city = city;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(
            String destination
    ) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(
            LocalDate startDate
    ) {
        this.startDate = startDate;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(
            Integer numberOfDays
    ) {
        this.numberOfDays = numberOfDays;
    }

    public Integer getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(
            Integer numberOfPersons
    ) {
        this.numberOfPersons =
                numberOfPersons;
    }

    public Integer getTravellers() {
        return travellers;
    }

    public void setTravellers(
            Integer travellers
    ) {
        this.travellers = travellers;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(
            BigDecimal totalBudget
    ) {
        this.totalBudget =
                totalBudget;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(
            BigDecimal budget
    ) {
        this.budget = budget;
    }

    public Integer getNearbyRadiusKm() {
        return nearbyRadiusKm;
    }

    public void setNearbyRadiusKm(
            Integer nearbyRadiusKm
    ) {
        this.nearbyRadiusKm =
                nearbyRadiusKm;
    }

    public TravelType getTravelType() {
        return travelType;
    }

    public void setTravelType(
            TravelType travelType
    ) {
        this.travelType =
                travelType;
    }

    public TravelType getTravelStyle() {
        return travelStyle;
    }

    public void setTravelStyle(
            TravelType travelStyle
    ) {
        this.travelStyle =
                travelStyle;
    }

    public List<String> getPreferredCategories() {
        return preferredCategories;
    }

    public void setPreferredCategories(
            List<String> preferredCategories
    ) {
        this.preferredCategories =
                preferredCategories == null
                        ? new ArrayList<>()
                        : preferredCategories;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(
            List<String> categories
    ) {
        this.categories =
                categories == null
                        ? new ArrayList<>()
                        : categories;
    }

    public List<Long> getPlaceIds() {
        return placeIds;
    }

    public void setPlaceIds(
            List<Long> placeIds
    ) {
        this.placeIds =
                placeIds == null
                        ? new ArrayList<>()
                        : placeIds;
    }

    public Long getSelectedPlaceId() {
        return selectedPlaceId;
    }

    public void setSelectedPlaceId(
            Long selectedPlaceId
    ) {
        this.selectedPlaceId =
                selectedPlaceId;
    }
}