package com.locallens.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripPlannerResponse {

    private Long itineraryId;

    private String title;

    private String city;

    private String destination;

    private LocalDate startDate;

    private Integer numberOfDays;

    private Integer numberOfPersons;

    private Integer travellers;

    private Integer nearbyRadiusKm;

    private Integer totalPlaces;

    private BigDecimal estimatedBudget;

    private BigDecimal userBudget;

    private BigDecimal totalBudget;

    private BigDecimal budget;

    private String travelStyle;

    private String travelType;

    private List<String> preferredCategories =
            new ArrayList<>();

    private List<String> categories =
            new ArrayList<>();

    private List<DayPlan> days =
            new ArrayList<>();

    public TripPlannerResponse() {
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(
            Long itineraryId
    ) {
        this.itineraryId =
                itineraryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(
            String title
    ) {
        this.title = title;
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
        this.destination =
                destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(
            LocalDate startDate
    ) {
        this.startDate =
                startDate;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(
            Integer numberOfDays
    ) {
        this.numberOfDays =
                numberOfDays;
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
        this.travellers =
                travellers;
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

    public Integer getTotalPlaces() {
        return totalPlaces;
    }

    public void setTotalPlaces(
            Integer totalPlaces
    ) {
        this.totalPlaces =
                totalPlaces;
    }

    public BigDecimal getEstimatedBudget() {
        return estimatedBudget;
    }

    public void setEstimatedBudget(
            BigDecimal estimatedBudget
    ) {
        this.estimatedBudget =
                estimatedBudget;
    }

    public BigDecimal getUserBudget() {
        return userBudget;
    }

    public void setUserBudget(
            BigDecimal userBudget
    ) {
        this.userBudget =
                userBudget;
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

    public String getTravelStyle() {
        return travelStyle;
    }

    public void setTravelStyle(
            String travelStyle
    ) {
        this.travelStyle =
                travelStyle;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(
            String travelType
    ) {
        this.travelType =
                travelType;
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

    public List<DayPlan> getDays() {
        return days;
    }

    public void setDays(
            List<DayPlan> days
    ) {
        this.days =
                days == null
                        ? new ArrayList<>()
                        : days;
    }

    /*
     * Represents one day of the generated itinerary.
     */
    public static class DayPlan {

        private Integer dayNumber;

        private String date;

        private String title;

        private List<PlaceInfo> places =
                new ArrayList<>();

        /*
         * Compatibility fields for the React frontend.
         */
        private List<PlaceInfo> activities =
                new ArrayList<>();

        private List<PlaceInfo> items =
                new ArrayList<>();

        public DayPlan() {
        }

        public Integer getDayNumber() {
            return dayNumber;
        }

        public void setDayNumber(
                Integer dayNumber
        ) {
            this.dayNumber =
                    dayNumber;
        }

        public String getDate() {
            return date;
        }

        public void setDate(
                String date
        ) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(
                String title
        ) {
            this.title = title;
        }

        public List<PlaceInfo> getPlaces() {
            return places;
        }

        public void setPlaces(
                List<PlaceInfo> places
        ) {
            this.places =
                    places == null
                            ? new ArrayList<>()
                            : places;
        }

        public List<PlaceInfo> getActivities() {
            return activities;
        }

        public void setActivities(
                List<PlaceInfo> activities
        ) {
            this.activities =
                    activities == null
                            ? new ArrayList<>()
                            : activities;
        }

        public List<PlaceInfo> getItems() {
            return items;
        }

        public void setItems(
                List<PlaceInfo> items
        ) {
            this.items =
                    items == null
                            ? new ArrayList<>()
                            : items;
        }
    }

    /*
     * Represents one place/activity inside a day.
     */
    public static class PlaceInfo {

        private Long id;

        private Long placeId;

        private String name;

        private String placeName;

        private String category;

        private String type;

        private String city;

        private String state;

        private String description;

        private String time;

        private String visitTime;

        private String suggestedTime;

        private Integer displayOrder;

        private BigDecimal estimatedCost;

        private BigDecimal averageRating;

        private Double latitude;

        private Double longitude;

        private String imageUrl;

        public PlaceInfo() {
        }

        public Long getId() {
            return id;
        }

        public void setId(
                Long id
        ) {
            this.id = id;
        }

        public Long getPlaceId() {
            return placeId;
        }

        public void setPlaceId(
                Long placeId
        ) {
            this.placeId =
                    placeId;
        }

        public String getName() {
            return name;
        }

        public void setName(
                String name
        ) {
            this.name = name;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(
                String placeName
        ) {
            this.placeName =
                    placeName;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(
                String category
        ) {
            this.category =
                    category;
        }

        public String getType() {
            return type;
        }

        public void setType(
                String type
        ) {
            this.type = type;
        }

        public String getCity() {
            return city;
        }

        public void setCity(
                String city
        ) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(
                String state
        ) {
            this.state = state;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(
                String description
        ) {
            this.description =
                    description;
        }

        public String getTime() {
            return time;
        }

        public void setTime(
                String time
        ) {
            this.time = time;
        }

        public String getVisitTime() {
            return visitTime;
        }

        public void setVisitTime(
                String visitTime
        ) {
            this.visitTime =
                    visitTime;
        }

        public String getSuggestedTime() {
            return suggestedTime;
        }

        public void setSuggestedTime(
                String suggestedTime
        ) {
            this.suggestedTime =
                    suggestedTime;
        }

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(
                Integer displayOrder
        ) {
            this.displayOrder =
                    displayOrder;
        }

        public BigDecimal getEstimatedCost() {
            return estimatedCost;
        }

        public void setEstimatedCost(
                BigDecimal estimatedCost
        ) {
            this.estimatedCost =
                    estimatedCost;
        }

        public BigDecimal getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(
                BigDecimal averageRating
        ) {
            this.averageRating =
                    averageRating;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(
                Double latitude
        ) {
            this.latitude =
                    latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(
                Double longitude
        ) {
            this.longitude =
                    longitude;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(
                String imageUrl
        ) {
            this.imageUrl =
                    imageUrl;
        }
    }
}