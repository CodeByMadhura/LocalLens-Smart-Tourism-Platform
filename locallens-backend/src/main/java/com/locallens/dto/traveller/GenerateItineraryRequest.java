package com.locallens.dto.traveller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GenerateItineraryRequest {

    @NotBlank(message = "Starting location is required")
    @Size(max = 150)
    private String startLocation;

    @NotBlank(message = "Destination is required")
    @Size(max = 150)
    private String destination;

    @NotNull(message = "Number of days is required")
    @Min(value = 1, message = "Number of days must be at least 1")
    @Max(value = 30, message = "Number of days cannot exceed 30")
    private Integer numberOfDays;

    @NotNull(message = "Number of persons is required")
    @Min(value = 1, message = "At least one person is required")
    @Max(value = 50, message = "Number of persons cannot exceed 50")
    private Integer numberOfPersons;

    @NotNull(message = "Budget is required")
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "Budget must be greater than zero"
    )
    private BigDecimal totalBudget;

    @Min(value = 1, message = "Nearby radius must be at least 1 km")
    @Max(value = 500, message = "Nearby radius cannot exceed 500 km")
    private Integer nearbyRadiusKm = 25;

    private String travelType;

    private List<String> preferredCategories = new ArrayList<>();

    public GenerateItineraryRequest() {
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public Integer getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(Integer numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public Integer getNearbyRadiusKm() {
        return nearbyRadiusKm;
    }

    public void setNearbyRadiusKm(Integer nearbyRadiusKm) {
        this.nearbyRadiusKm = nearbyRadiusKm;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
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
}