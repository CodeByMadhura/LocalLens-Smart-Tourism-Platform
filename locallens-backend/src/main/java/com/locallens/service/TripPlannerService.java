package com.locallens.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.locallens.dto.trip.TripPlannerRequest;
import com.locallens.dto.trip.TripPlannerResponse;
import com.locallens.entities.Itinerary;
import com.locallens.entities.ItineraryItem;
import com.locallens.entities.Place;
import com.locallens.entities.PlaceImage;
import com.locallens.entities.User;
import com.locallens.enums.ApprovalStatus;
import com.locallens.enums.TravelType;
import com.locallens.repository.ItineraryRepository;
import com.locallens.repository.PlaceRepository;
import com.locallens.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripPlannerService {

    private static final int DEFAULT_NUMBER_OF_DAYS = 1;

    private static final int DEFAULT_NUMBER_OF_PERSONS = 1;

    private static final int DEFAULT_NEARBY_RADIUS_KM = 25;

    private static final int MAX_PLACES_PER_DAY = 4;

    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER =
            DateTimeFormatter.ofPattern(
                    "MMM dd, yyyy",
                    Locale.ENGLISH
            );

    private static final DateTimeFormatter DISPLAY_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(
                    "hh:mm a",
                    Locale.ENGLISH
            );

    private final PlaceRepository placeRepository;

    private final ItineraryRepository itineraryRepository;

    private final UserRepository userRepository;

    // =========================================================
    // GENERATE ITINERARY PREVIEW
    // =========================================================

    public TripPlannerResponse generateItinerary(
            TripPlannerRequest request,
            Long travellerId
    ) {

        validateTraveller(travellerId);
        validateTripPlannerRequest(request);

        String destination =
                resolveDestination(request);

        int numberOfDays =
                resolveNumberOfDays(request);

        int numberOfPersons =
                resolveNumberOfPersons(request);

        int nearbyRadiusKm =
                resolveNearbyRadius(request);

        BigDecimal totalBudget =
                resolveBudget(request);

        List<String> selectedCategories =
                resolveCategories(request);

        List<Long> selectedPlaceIds =
                resolveSelectedPlaceIds(request);

        List<Place> approvedCityPlaces =
                placeRepository
                        .findByCityIgnoreCaseAndStatusOrderByCreatedAtDesc(
                                destination,
                                ApprovalStatus.APPROVED
                        );

        if (
                approvedCityPlaces == null
                        || approvedCityPlaces.isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "No approved places are available in "
                            + destination
                            + "."
            );
        }

        List<Place> filteredPlaces =
                approvedCityPlaces.stream()
                        .filter(place ->
                                matchesSelectedPlaceIds(
                                        place,
                                        selectedPlaceIds
                                )
                        )
                        .filter(place ->
                                matchesCategory(
                                        place,
                                        selectedCategories
                                )
                        )
                        .sorted(
                                Comparator
                                        .comparing(
                                                this::safeEstimatedCost
                                        )
                                        .thenComparing(
                                                place ->
                                                        safeText(
                                                                place.getName()
                                                        ),
                                                String.CASE_INSENSITIVE_ORDER
                                        )
                        )
                        .toList();

        if (filteredPlaces.isEmpty()) {
            throw new IllegalArgumentException(
                    "No approved places match the selected city and categories."
            );
        }

        List<Place> selectedPlaces =
                selectPlacesWithinBudget(
                        filteredPlaces,
                        numberOfDays,
                        numberOfPersons,
                        totalBudget
                );

        if (selectedPlaces.isEmpty()) {
            throw new IllegalArgumentException(
                    "No approved places can be included within the entered budget."
            );
        }

        BigDecimal estimatedTripCost =
                calculateEstimatedTripCost(
                        selectedPlaces,
                        numberOfPersons
                );

        List<List<Place>> dayWisePlaces =
                distributePlacesAcrossDays(
                        selectedPlaces,
                        numberOfDays
                );

        return buildPreviewResponse(
                request,
                destination,
                numberOfDays,
                numberOfPersons,
                nearbyRadiusKm,
                totalBudget,
                estimatedTripCost,
                selectedCategories,
                dayWisePlaces
        );
    }

    // =========================================================
    // SAVE ITINERARY
    // =========================================================

    @Transactional
    public TripPlannerResponse saveItinerary(
            TripPlannerResponse generatedItinerary,
            Long travellerId
    ) {

        if (generatedItinerary == null) {
            throw new IllegalArgumentException(
                    "Itinerary information is required."
            );
        }

        User traveller =
                validateTraveller(travellerId);

        String destination =
                firstNonBlank(
                        generatedItinerary.getDestination(),
                        generatedItinerary.getCity()
                );

        if (destination == null) {
            throw new IllegalArgumentException(
                    "Itinerary destination is required."
            );
        }

        Integer numberOfDays =
                generatedItinerary.getNumberOfDays();

        if (
                numberOfDays == null
                        || numberOfDays < 1
                        || numberOfDays > 30
        ) {
            throw new IllegalArgumentException(
                    "Number of days must be between 1 and 30."
            );
        }

        if (
                generatedItinerary.getDays() == null
                        || generatedItinerary
                        .getDays()
                        .isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "The itinerary must contain at least one day."
            );
        }

        Itinerary itinerary =
                new Itinerary();

        String title =
                trimToNull(
                        generatedItinerary.getTitle()
                );

        itinerary.setTitle(
                title != null
                        ? title
                        : numberOfDays
                        + "-Day Trip to "
                        + destination
        );

        itinerary.setDestination(destination);
        itinerary.setNumberOfDays(numberOfDays);

        itinerary.setStartDate(
                generatedItinerary.getStartDate()
        );

        itinerary.setBudget(
                resolveResponseBudget(
                        generatedItinerary
                )
        );

        String responseTravelType =
                firstNonBlank(
                        generatedItinerary.getTravelType(),
                        generatedItinerary.getTravelStyle()
                );

        itinerary.setTravelType(
                resolveTravelType(
                        responseTravelType
                )
        );

        itinerary.setTraveller(traveller);

        List<String> categories =
                resolveResponseCategories(
                        generatedItinerary
                );

        if (!categories.isEmpty()) {
            itinerary.setInterests(
                    String.join(
                            ",",
                            categories
                    )
            );
        }

        for (
                TripPlannerResponse.DayPlan day :
                generatedItinerary.getDays()
        ) {

            if (day == null) {
                continue;
            }

            List<TripPlannerResponse.PlaceInfo> dayPlaces =
                    resolveDayPlaces(day);

            if (dayPlaces.isEmpty()) {
                continue;
            }

            int generatedDisplayOrder = 1;

            for (
                    TripPlannerResponse.PlaceInfo placeInfo :
                    dayPlaces
            ) {

                if (placeInfo == null) {
                    continue;
                }

                Long placeId =
                        placeInfo.getPlaceId();

                if (placeId == null) {
                    placeId =
                            placeInfo.getId();
                }

                if (placeId == null) {
                    continue;
                }

                final Long resolvedPlaceId =
                        placeId;

                /*
                 * Saving an itinerary needs only the Place entity.
                 * Do not load images, creator and details here.
                 */
                Place place =
                        placeRepository
                                .findById(
                                        resolvedPlaceId
                                )
                                .orElseThrow(() ->
                                        new IllegalArgumentException(
                                                "Place not found with ID: "
                                                        + resolvedPlaceId
                                        )
                                );

                if (
                        place.getStatus()
                                != ApprovalStatus.APPROVED
                ) {
                    throw new IllegalArgumentException(
                            "Place "
                                    + place.getName()
                                    + " is not approved."
                    );
                }

                ItineraryItem item =
                        new ItineraryItem();

                Integer dayNumber =
                        day.getDayNumber();

                item.setDayNumber(
                        dayNumber != null
                                ? dayNumber
                                : 1
                );

                Integer requestedOrder =
                        placeInfo.getDisplayOrder();

                int finalDisplayOrder =
                        requestedOrder != null
                                ? requestedOrder
                                : generatedDisplayOrder;

                item.setDisplayOrder(
                        finalDisplayOrder
                );

                item.setPlace(place);

                String requestedTime =
                        firstNonBlank(
                                placeInfo.getTime(),
                                placeInfo.getVisitTime(),
                                placeInfo.getSuggestedTime()
                        );

                item.setPlannedTime(
                        parseTimeOrDefault(
                                requestedTime,
                                finalDisplayOrder
                        )
                );

                item.setNotes(
                        trimToNull(
                                placeInfo.getDescription()
                        )
                );

                /*
                 * addItem() also executes:
                 * item.setItinerary(itinerary)
                 */
                itinerary.addItem(item);

                generatedDisplayOrder++;
            }
        }

        if (
                itinerary.getItems() == null
                        || itinerary
                        .getItems()
                        .isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "The itinerary does not contain valid approved places."
            );
        }

        /*
         * Force Hibernate to execute the INSERT statements now.
         * Any database error will be caught by GlobalExceptionHandler.
         */
        Itinerary savedItinerary =
                itineraryRepository.saveAndFlush(
                        itinerary
                );

        generatedItinerary.setItineraryId(
                savedItinerary.getId()
        );

        return generatedItinerary;
    }

    // =========================================================
    // GET ALL TRAVELLER ITINERARIES
    // =========================================================

    @Transactional(readOnly = true)
    public List<TripPlannerResponse> getMyItineraries(
            Long travellerId
    ) {

        validateTraveller(travellerId);

        return itineraryRepository
                .findByTravellerIdOrderByCreatedAtDesc(
                        travellerId
                )
                .stream()
                .map(this::convertItineraryToResponse)
                .toList();
    }

    // =========================================================
    // GET ONE ITINERARY
    // =========================================================

    @Transactional(readOnly = true)
    public TripPlannerResponse getItineraryById(
            Long travellerId,
            Long itineraryId
    ) {

        validateTraveller(travellerId);

        if (itineraryId == null) {
            throw new IllegalArgumentException(
                    "Itinerary ID is required."
            );
        }

        Itinerary itinerary =
                itineraryRepository
                        .findWithItemsById(
                                itineraryId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Itinerary not found with ID: "
                                                + itineraryId
                                )
                        );

        validateItineraryOwnership(
                itinerary,
                travellerId
        );

        return convertItineraryToResponse(
                itinerary
        );
    }

    // =========================================================
    // DELETE ITINERARY
    // =========================================================

    @Transactional
    public void deleteItinerary(
            Long travellerId,
            Long itineraryId
    ) {

        validateTraveller(travellerId);

        if (itineraryId == null) {
            throw new IllegalArgumentException(
                    "Itinerary ID is required."
            );
        }

        Itinerary itinerary =
                itineraryRepository
                        .findWithItemsById(
                                itineraryId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Itinerary not found with ID: "
                                                + itineraryId
                                )
                        );

        validateItineraryOwnership(
                itinerary,
                travellerId
        );

        itineraryRepository.delete(
                itinerary
        );

        itineraryRepository.flush();
    }

    // =========================================================
    // GENERATE PREVIEW RESPONSE
    // =========================================================

    private TripPlannerResponse buildPreviewResponse(
            TripPlannerRequest request,
            String destination,
            int numberOfDays,
            int numberOfPersons,
            int nearbyRadiusKm,
            BigDecimal totalBudget,
            BigDecimal estimatedTripCost,
            List<String> selectedCategories,
            List<List<Place>> dayWisePlaces
    ) {

        TripPlannerResponse response =
                new TripPlannerResponse();

        response.setTitle(
                numberOfDays
                        + "-Day Trip to "
                        + destination
        );

        response.setCity(destination);
        response.setDestination(destination);

        response.setStartDate(
                request.getStartDate()
        );

        response.setNumberOfDays(
                numberOfDays
        );

        response.setNumberOfPersons(
                numberOfPersons
        );

        response.setTravellers(
                numberOfPersons
        );

        response.setNearbyRadiusKm(
                nearbyRadiusKm
        );

        response.setTotalPlaces(
                dayWisePlaces.stream()
                        .mapToInt(List::size)
                        .sum()
        );

        response.setEstimatedBudget(
                estimatedTripCost
        );

        response.setUserBudget(
                totalBudget
        );

        response.setTotalBudget(
                totalBudget
        );

        response.setBudget(
                totalBudget
        );

        String travelType =
                resolveTravelTypeName(request);

        response.setTravelType(
                travelType
        );

        response.setTravelStyle(
                travelType
        );

        response.setCategories(
                new ArrayList<>(
                        selectedCategories
                )
        );

        response.setPreferredCategories(
                new ArrayList<>(
                        selectedCategories
                )
        );

        for (
                int dayIndex = 0;
                dayIndex < dayWisePlaces.size();
                dayIndex++
        ) {

            int dayNumber =
                    dayIndex + 1;

            TripPlannerResponse.DayPlan dayPlan =
                    new TripPlannerResponse.DayPlan();

            dayPlan.setDayNumber(
                    dayNumber
            );

            dayPlan.setTitle(
                    "Explore "
                            + destination
                            + " - Day "
                            + dayNumber
            );

            LocalDate startDate =
                    request.getStartDate();

            if (startDate != null) {

                LocalDate dayDate =
                        startDate.plusDays(
                                dayIndex
                        );

                dayPlan.setDate(
                        dayDate.format(
                                DISPLAY_DATE_FORMATTER
                        )
                );

            } else {

                dayPlan.setDate(
                        "Day " + dayNumber
                );
            }

            List<Place> placesForDay =
                    dayWisePlaces.get(
                            dayIndex
                    );

            for (
                    int placeIndex = 0;
                    placeIndex < placesForDay.size();
                    placeIndex++
            ) {

                Place place =
                        placesForDay.get(
                                placeIndex
                        );

                int displayOrder =
                        placeIndex + 1;

                LocalTime plannedTime =
                        getTimeForPosition(
                                displayOrder
                        );

                String formattedTime =
                        plannedTime.format(
                                DISPLAY_TIME_FORMATTER
                        );

                TripPlannerResponse.PlaceInfo placeInfo =
                        convertPlaceToPlaceInfo(
                                place,
                                displayOrder,
                                formattedTime,
                                createVisitDescription(
                                        place,
                                        displayOrder
                                )
                        );

                dayPlan.getPlaces().add(
                        placeInfo
                );

                dayPlan.getActivities().add(
                        placeInfo
                );
            }

            response.getDays().add(
                    dayPlan
            );
        }

        return response;
    }

    // =========================================================
    // CONVERT SAVED ITINERARY TO RESPONSE
    // =========================================================

    private TripPlannerResponse convertItineraryToResponse(
            Itinerary itinerary
    ) {

        TripPlannerResponse response =
                new TripPlannerResponse();

        response.setItineraryId(
                itinerary.getId()
        );

        response.setTitle(
                itinerary.getTitle()
        );

        response.setCity(
                itinerary.getDestination()
        );

        response.setDestination(
                itinerary.getDestination()
        );

        response.setStartDate(
                itinerary.getStartDate()
        );

        response.setNumberOfDays(
                itinerary.getNumberOfDays()
        );

        response.setBudget(
                itinerary.getBudget()
        );

        response.setTotalBudget(
                itinerary.getBudget()
        );

        response.setUserBudget(
                itinerary.getBudget()
        );

        response.setEstimatedBudget(
                calculateSavedItineraryCost(
                        itinerary
                )
        );

        String travelType =
                itinerary.getTravelType() == null
                        ? null
                        : itinerary
                        .getTravelType()
                        .name();

        response.setTravelType(
                travelType
        );

        response.setTravelStyle(
                travelType
        );

        List<String> categories =
                splitInterests(
                        itinerary.getInterests()
                );

        response.setCategories(
                new ArrayList<>(
                        categories
                )
        );

        response.setPreferredCategories(
                new ArrayList<>(
                        categories
                )
        );

        List<ItineraryItem> items =
                itinerary.getItems() == null
                        ? new ArrayList<>()
                        : itinerary.getItems();

        response.setTotalPlaces(
                items.size()
        );

        int numberOfDays =
                itinerary.getNumberOfDays() == null
                        ? 1
                        : itinerary.getNumberOfDays();

        for (
                int dayNumber = 1;
                dayNumber <= numberOfDays;
                dayNumber++
        ) {

            TripPlannerResponse.DayPlan dayPlan =
                    new TripPlannerResponse.DayPlan();

            dayPlan.setDayNumber(
                    dayNumber
            );

            dayPlan.setTitle(
                    "Explore "
                            + itinerary.getDestination()
                            + " - Day "
                            + dayNumber
            );

            if (itinerary.getStartDate() != null) {

                LocalDate dayDate =
                        itinerary.getStartDate()
                                .plusDays(
                                        dayNumber - 1L
                                );

                dayPlan.setDate(
                        dayDate.format(
                                DISPLAY_DATE_FORMATTER
                        )
                );

            } else {

                dayPlan.setDate(
                        "Day " + dayNumber
                );
            }

            final int currentDayNumber =
                    dayNumber;

            items.stream()
                    .filter(item ->
                            item != null
                                    && item.getDayNumber() != null
                                    && item.getDayNumber()
                                    .equals(
                                            currentDayNumber
                                    )
                    )
                    .sorted(
                            Comparator.comparing(
                                    item ->
                                            item.getDisplayOrder() == null
                                                    ? Integer.MAX_VALUE
                                                    : item.getDisplayOrder()
                            )
                    )
                    .forEach(item -> {

                        Place place =
                                item.getPlace();

                        if (place == null) {
                            return;
                        }

                        String formattedTime =
                                item.getPlannedTime() == null
                                        ? null
                                        : item.getPlannedTime()
                                        .format(
                                                DISPLAY_TIME_FORMATTER
                                        );

                        TripPlannerResponse.PlaceInfo placeInfo =
                                convertPlaceToPlaceInfo(
                                        place,
                                        item.getDisplayOrder(),
                                        formattedTime,
                                        item.getNotes()
                                );

                        dayPlan.getPlaces().add(
                                placeInfo
                        );

                        dayPlan.getActivities().add(
                                placeInfo
                        );
                    });

            response.getDays().add(
                    dayPlan
            );
        }

        return response;
    }

    private TripPlannerResponse.PlaceInfo convertPlaceToPlaceInfo(
            Place place,
            Integer displayOrder,
            String formattedTime,
            String description
    ) {

        TripPlannerResponse.PlaceInfo placeInfo =
                new TripPlannerResponse.PlaceInfo();

        placeInfo.setId(
                place.getId()
        );

        placeInfo.setPlaceId(
                place.getId()
        );

        placeInfo.setName(
                place.getName()
        );

        placeInfo.setPlaceName(
                place.getName()
        );

        placeInfo.setCity(
                place.getCity()
        );

        placeInfo.setState(
                place.getState()
        );

        placeInfo.setCategory(
                place.getCategory()
        );

        placeInfo.setType(
                place.getCategory()
        );

        placeInfo.setEstimatedCost(
                safeEstimatedCost(place)
        );

        placeInfo.setAverageRating(
                place.getAverageRating()
        );

        placeInfo.setLatitude(
                toDouble(
                        place.getLatitude()
                )
        );

        placeInfo.setLongitude(
                toDouble(
                        place.getLongitude()
                )
        );

        placeInfo.setDisplayOrder(
                displayOrder
        );

        placeInfo.setTime(
                formattedTime
        );

        placeInfo.setVisitTime(
                formattedTime
        );

        placeInfo.setSuggestedTime(
                formattedTime
        );

        placeInfo.setDescription(
                description
        );

        placeInfo.setImageUrl(
                resolvePrimaryImageUrl(
                        place
                )
        );

        return placeInfo;
    }

    // =========================================================
    // PLACE SELECTION
    // =========================================================

    private List<Place> selectPlacesWithinBudget(
            List<Place> places,
            int numberOfDays,
            int numberOfPersons,
            BigDecimal totalBudget
    ) {

        List<Place> selectedPlaces =
                new ArrayList<>();

        BigDecimal currentCost =
                BigDecimal.ZERO;

        int maximumPlaces =
                numberOfDays
                        * MAX_PLACES_PER_DAY;

        for (Place place : places) {

            if (
                    selectedPlaces.size()
                            >= maximumPlaces
            ) {
                break;
            }

            BigDecimal placeTotalCost =
                    safeEstimatedCost(place)
                            .multiply(
                                    BigDecimal.valueOf(
                                            numberOfPersons
                                    )
                            );

            BigDecimal nextCost =
                    currentCost.add(
                            placeTotalCost
                    );

            if (
                    nextCost.compareTo(
                            totalBudget
                    ) <= 0
            ) {
                selectedPlaces.add(place);
                currentCost = nextCost;
            }
        }

        return selectedPlaces;
    }

    private BigDecimal calculateEstimatedTripCost(
            List<Place> places,
            int numberOfPersons
    ) {

        return places.stream()
                .map(this::safeEstimatedCost)
                .map(cost ->
                        cost.multiply(
                                BigDecimal.valueOf(
                                        numberOfPersons
                                )
                        )
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    private BigDecimal calculateSavedItineraryCost(
            Itinerary itinerary
    ) {

        if (
                itinerary == null
                        || itinerary.getItems() == null
        ) {
            return BigDecimal.ZERO;
        }

        return itinerary.getItems()
                .stream()
                .filter(item ->
                        item != null
                                && item.getPlace() != null
                )
                .map(item ->
                        safeEstimatedCost(
                                item.getPlace()
                        )
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    private List<List<Place>> distributePlacesAcrossDays(
            List<Place> places,
            int numberOfDays
    ) {

        List<List<Place>> result =
                new ArrayList<>();

        for (
                int index = 0;
                index < numberOfDays;
                index++
        ) {
            result.add(
                    new ArrayList<>()
            );
        }

        for (
                int index = 0;
                index < places.size();
                index++
        ) {

            int dayIndex =
                    index % numberOfDays;

            result.get(dayIndex)
                    .add(
                            places.get(index)
                    );
        }

        return result;
    }

    private boolean matchesSelectedPlaceIds(
            Place place,
            List<Long> selectedPlaceIds
    ) {

        if (
                selectedPlaceIds == null
                        || selectedPlaceIds.isEmpty()
        ) {
            return true;
        }

        return selectedPlaceIds.contains(
                place.getId()
        );
    }

    private boolean matchesCategory(
            Place place,
            List<String> categories
    ) {

        if (
                categories == null
                        || categories.isEmpty()
        ) {
            return true;
        }

        String placeCategory =
                trimToNull(
                        place.getCategory()
                );

        if (placeCategory == null) {
            return false;
        }

        return categories.stream()
                .filter(category ->
                        trimToNull(category) != null
                )
                .anyMatch(category ->
                        placeCategory.equalsIgnoreCase(
                                category.trim()
                        )
                );
    }

    // =========================================================
    // VALIDATION
    // =========================================================

    private User validateTraveller(
            Long travellerId
    ) {

        if (travellerId == null) {
            throw new IllegalArgumentException(
                    "Traveller ID is required."
            );
        }

        User traveller =
                userRepository
                        .findById(travellerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Traveller account was not found."
                                )
                        );

        if (!traveller.isActive()) {
            throw new IllegalStateException(
                    "Traveller account is inactive."
            );
        }

        if (!traveller.isTraveller()) {
            throw new IllegalStateException(
                    "Only travellers can access itineraries."
            );
        }

        return traveller;
    }

    private void validateItineraryOwnership(
            Itinerary itinerary,
            Long travellerId
    ) {

        if (
                itinerary == null
                        || itinerary.getTraveller() == null
                        || itinerary.getTraveller()
                        .getId() == null
                        || !itinerary.getTraveller()
                        .getId()
                        .equals(travellerId)
        ) {
            throw new IllegalStateException(
                    "You are not allowed to access this itinerary."
            );
        }
    }

    private void validateTripPlannerRequest(
            TripPlannerRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Trip information is required."
            );
        }

        String destination =
                resolveDestination(request);

        if (destination == null) {
            throw new IllegalArgumentException(
                    "City is required."
            );
        }

        if (request.getStartDate() == null) {
            throw new IllegalArgumentException(
                    "Start date is required."
            );
        }

        if (
                request.getStartDate()
                        .isBefore(
                                LocalDate.now()
                        )
        ) {
            throw new IllegalArgumentException(
                    "Start date cannot be in the past."
            );
        }

        int numberOfDays =
                resolveNumberOfDays(request);

        if (
                numberOfDays < 1
                        || numberOfDays > 30
        ) {
            throw new IllegalArgumentException(
                    "Number of days must be between 1 and 30."
            );
        }

        int numberOfPersons =
                resolveNumberOfPersons(request);

        if (
                numberOfPersons < 1
                        || numberOfPersons > 50
        ) {
            throw new IllegalArgumentException(
                    "Number of persons must be between 1 and 50."
            );
        }

        BigDecimal budget =
                resolveBudget(request);

        if (
                budget.compareTo(
                        BigDecimal.ZERO
                ) <= 0
        ) {
            throw new IllegalArgumentException(
                    "Budget must be greater than zero."
            );
        }

        int nearbyRadius =
                resolveNearbyRadius(request);

        if (
                nearbyRadius < 1
                        || nearbyRadius > 500
        ) {
            throw new IllegalArgumentException(
                    "Nearby radius must be between 1 and 500 kilometres."
            );
        }
    }

    // =========================================================
    // REQUEST HELPERS
    // =========================================================

    private String resolveDestination(
            TripPlannerRequest request
    ) {

        String destination =
                trimToNull(
                        request.getDestination()
                );

        if (destination != null) {
            return destination;
        }

        return trimToNull(
                request.getCity()
        );
    }

    private int resolveNumberOfDays(
            TripPlannerRequest request
    ) {

        Integer value =
                request.getNumberOfDays();

        return value != null
                ? value
                : DEFAULT_NUMBER_OF_DAYS;
    }

    private int resolveNumberOfPersons(
            TripPlannerRequest request
    ) {

        Integer value =
                request.getNumberOfPersons();

        if (value == null) {
            value =
                    request.getTravellers();
        }

        return value != null
                ? value
                : DEFAULT_NUMBER_OF_PERSONS;
    }

    private int resolveNearbyRadius(
            TripPlannerRequest request
    ) {

        Integer value =
                request.getNearbyRadiusKm();

        return value != null
                ? value
                : DEFAULT_NEARBY_RADIUS_KM;
    }

    private BigDecimal resolveBudget(
            TripPlannerRequest request
    ) {

        BigDecimal value =
                request.getTotalBudget();

        if (value == null) {
            value =
                    request.getBudget();
        }

        return value != null
                ? value
                : BigDecimal.ZERO;
    }

    private List<String> resolveCategories(
            TripPlannerRequest request
    ) {

        List<String> categories =
                request.getPreferredCategories();

        if (
                categories == null
                        || categories.isEmpty()
        ) {
            categories =
                    request.getCategories();
        }

        if (categories == null) {
            return new ArrayList<>();
        }

        return categories.stream()
                .filter(value ->
                        trimToNull(value) != null
                )
                .map(String::trim)
                .distinct()
                .toList();
    }

    private List<Long> resolveSelectedPlaceIds(
            TripPlannerRequest request
    ) {

        Set<Long> placeIds =
                new HashSet<>();

        if (request.getPlaceIds() != null) {
            placeIds.addAll(
                    request.getPlaceIds()
            );
        }

        if (
                request.getSelectedPlaceId()
                        != null
        ) {
            placeIds.add(
                    request.getSelectedPlaceId()
            );
        }

        placeIds.remove(null);

        return new ArrayList<>(
                placeIds
        );
    }

    private String resolveTravelTypeName(
            TripPlannerRequest request
    ) {

        if (request.getTravelType() != null) {
            return request
                    .getTravelType()
                    .name();
        }

        if (request.getTravelStyle() != null) {
            return request
                    .getTravelStyle()
                    .name();
        }

        return TravelType.SOLO.name();
    }

    // =========================================================
    // RESPONSE HELPERS
    // =========================================================

    private BigDecimal resolveResponseBudget(
            TripPlannerResponse response
    ) {

        if (response.getUserBudget() != null) {
            return response.getUserBudget();
        }

        if (response.getTotalBudget() != null) {
            return response.getTotalBudget();
        }

        if (response.getBudget() != null) {
            return response.getBudget();
        }

        if (
                response.getEstimatedBudget()
                        != null
        ) {
            return response.getEstimatedBudget();
        }

        return BigDecimal.ZERO;
    }

    private List<String> resolveResponseCategories(
            TripPlannerResponse response
    ) {

        if (
                response.getPreferredCategories() != null
                        && !response
                        .getPreferredCategories()
                        .isEmpty()
        ) {
            return response
                    .getPreferredCategories();
        }

        if (response.getCategories() != null) {
            return response.getCategories();
        }

        return new ArrayList<>();
    }

    private List<TripPlannerResponse.PlaceInfo> resolveDayPlaces(
            TripPlannerResponse.DayPlan day
    ) {

        if (
                day.getPlaces() != null
                        && !day.getPlaces().isEmpty()
        ) {
            return day.getPlaces();
        }

        if (
                day.getActivities() != null
                        && !day
                        .getActivities()
                        .isEmpty()
        ) {
            return day.getActivities();
        }

        if (
                day.getItems() != null
                        && !day.getItems().isEmpty()
        ) {
            return day.getItems();
        }

        return new ArrayList<>();
    }

    // =========================================================
    // PLACE HELPERS
    // =========================================================

    private BigDecimal safeEstimatedCost(
            Place place
    ) {

        if (
                place == null
                        || place.getEstimatedCost()
                        == null
        ) {
            return BigDecimal.ZERO;
        }

        return place.getEstimatedCost();
    }

    private Double toDouble(
            Number value
    ) {

        return value == null
                ? null
                : value.doubleValue();
    }

    private String resolvePrimaryImageUrl(
            Place place
    ) {

        if (
                place == null
                        || place.getImages() == null
                        || place.getImages().isEmpty()
        ) {
            return null;
        }

        PlaceImage selectedImage =
                place.getImages()
                        .stream()
                        .filter(image ->
                                image != null
                                        && image.isPrimaryImage()
                                        && trimToNull(
                                                image.getImageUrl()
                                        ) != null
                        )
                        .findFirst()
                        .orElseGet(() ->
                                place.getImages()
                                        .stream()
                                        .filter(image ->
                                                image != null
                                                        && trimToNull(
                                                        image.getImageUrl()
                                                ) != null
                                        )
                                        .findFirst()
                                        .orElse(null)
                        );

        if (selectedImage == null) {
            return null;
        }

        return normalizeImageUrl(
                selectedImage.getImageUrl()
        );
    }

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
    // TIME HELPERS
    // =========================================================

    private LocalTime parseTimeOrDefault(
            String time,
            int displayOrder
    ) {

        String normalized =
                trimToNull(time);

        if (normalized != null) {

            try {
                return LocalTime.parse(
                        normalized,
                        DISPLAY_TIME_FORMATTER
                );
            } catch (Exception ignored) {
                // Try ISO LocalTime next.
            }

            try {
                return LocalTime.parse(
                        normalized
                );
            } catch (Exception ignored) {
                // Use default generated time.
            }
        }

        return getTimeForPosition(
                displayOrder
        );
    }

    private LocalTime getTimeForPosition(
            int position
    ) {

        return switch (position) {
            case 1 -> LocalTime.of(9, 0);
            case 2 -> LocalTime.of(12, 30);
            case 3 -> LocalTime.of(15, 30);
            case 4 -> LocalTime.of(18, 0);
            default -> LocalTime.of(19, 30);
        };
    }

    private String createVisitDescription(
            Place place,
            int position
    ) {

        return switch (position) {

            case 1 ->
                    "Start the day by visiting "
                            + place.getName()
                            + ".";

            case 2 ->
                    "Continue the trip with "
                            + place.getName()
                            + " around midday.";

            case 3 ->
                    "Explore "
                            + place.getName()
                            + " during the afternoon.";

            case 4 ->
                    "Finish the day's sightseeing at "
                            + place.getName()
                            + ".";

            default ->
                    "Visit "
                            + place.getName()
                            + ".";
        };
    }

    private TravelType resolveTravelType(
            String value
    ) {

        String normalized =
                trimToNull(value);

        if (normalized == null) {
            return TravelType.SOLO;
        }

        try {
            return TravelType.valueOf(
                    normalized.toUpperCase(
                            Locale.ENGLISH
                    )
            );

        } catch (IllegalArgumentException exception) {

            return TravelType.SOLO;
        }
    }

    // =========================================================
    // STRING AND LIST HELPERS
    // =========================================================

    private List<String> splitInterests(
            String interests
    ) {

        String value =
                trimToNull(interests);

        if (value == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(
                        value.split(",")
                )
                .map(String::trim)
                .filter(item ->
                        !item.isBlank()
                )
                .distinct()
                .toList();
    }

    private String firstNonBlank(
            String... values
    ) {

        if (values == null) {
            return null;
        }

        for (String value : values) {

            String normalized =
                    trimToNull(value);

            if (normalized != null) {
                return normalized;
            }
        }

        return null;
    }

    private String safeText(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }

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
}