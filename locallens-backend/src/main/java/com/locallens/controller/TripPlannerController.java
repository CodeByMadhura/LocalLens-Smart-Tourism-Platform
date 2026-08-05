package com.locallens.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.locallens.dto.trip.TripPlannerRequest;
import com.locallens.dto.trip.TripPlannerResponse;
import com.locallens.entities.User;
import com.locallens.repository.UserRepository;
import com.locallens.service.TripPlannerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/traveller/itineraries")
@RequiredArgsConstructor
public class TripPlannerController {

    private final TripPlannerService tripPlannerService;

    private final UserRepository userRepository;

    /**
     * Generates an itinerary preview.
     *
     * POST /api/traveller/itineraries/generate
     */
    @PostMapping("/generate")
    public ResponseEntity<TripPlannerResponse> generateItinerary(
            Authentication authentication,
            @Valid @RequestBody TripPlannerRequest request
    ) {

        Long travellerId =
                getAuthenticatedTravellerId(
                        authentication
                );

        TripPlannerResponse response =
                tripPlannerService.generateItinerary(
                        request,
                        travellerId
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Saves a generated itinerary.
     *
     * POST /api/traveller/itineraries
     */
    @PostMapping
    public ResponseEntity<TripPlannerResponse> saveItinerary(
            Authentication authentication,
            @RequestBody TripPlannerResponse request
    ) {

        Long travellerId =
                getAuthenticatedTravellerId(
                        authentication
                );

        TripPlannerResponse response =
                tripPlannerService.saveItinerary(
                        request,
                        travellerId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Returns all itineraries saved by the logged-in traveller.
     *
     * GET /api/traveller/itineraries
     */
    @GetMapping
    public ResponseEntity<List<TripPlannerResponse>>
            getMyItineraries(
                    Authentication authentication
            ) {

        Long travellerId =
                getAuthenticatedTravellerId(
                        authentication
                );

        List<TripPlannerResponse> responses =
                tripPlannerService.getMyItineraries(
                        travellerId
                );

        return ResponseEntity.ok(responses);
    }

    /**
     * Returns one saved itinerary belonging to the logged-in traveller.
     *
     * GET /api/traveller/itineraries/{itineraryId}
     */
    @GetMapping("/{itineraryId:\\d+}")
    public ResponseEntity<TripPlannerResponse> getItineraryById(
            Authentication authentication,
            @PathVariable("itineraryId")
            Long itineraryId
    ) {

        Long travellerId =
                getAuthenticatedTravellerId(
                        authentication
                );

        TripPlannerResponse response =
                tripPlannerService.getItineraryById(
                        travellerId,
                        itineraryId
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Deletes one saved itinerary belonging to the logged-in traveller.
     *
     * DELETE /api/traveller/itineraries/{itineraryId}
     */
    @DeleteMapping("/{itineraryId:\\d+}")
    public ResponseEntity<Void> deleteItinerary(
            Authentication authentication,
            @PathVariable("itineraryId")
            Long itineraryId
    ) {

        Long travellerId =
                getAuthenticatedTravellerId(
                        authentication
                );

        tripPlannerService.deleteItinerary(
                travellerId,
                itineraryId
        );

        return ResponseEntity.noContent().build();
    }

    private Long getAuthenticatedTravellerId(
            Authentication authentication
    ) {

        if (
                authentication == null
                || !authentication.isAuthenticated()
        ) {
            throw new IllegalArgumentException(
                    "Authentication is required."
            );
        }

        String email =
                authentication.getName();

        if (
                email == null
                || email.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Authenticated user email is missing."
            );
        }

        User user =
                userRepository
                        .findByEmailIgnoreCase(
                                email.trim()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Authenticated traveller was not found."
                                )
                        );

        if (!user.isTraveller()) {
            throw new IllegalStateException(
                    "Only travellers can access itineraries."
            );
        }

        if (!user.isActive()) {
            throw new IllegalStateException(
                    "Traveller account is inactive."
            );
        }

        return user.getId();
    }
}