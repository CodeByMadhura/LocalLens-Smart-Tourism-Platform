package com.locallens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.locallens.entities.Itinerary;

@Repository
public interface ItineraryRepository
        extends JpaRepository<Itinerary, Long> {

    /*
     * Load the itinerary, traveller, items and each item's place.
     *
     * Do not include "items.place.images" here because both
     * Itinerary.items and Place.images are List collections.
     * Fetching both simultaneously causes MultipleBagFetchException.
     */
    @EntityGraph(attributePaths = {
            "traveller",
            "items",
            "items.place"
    })
    List<Itinerary>
            findByTravellerIdOrderByCreatedAtDesc(
                    Long travellerId
            );

    /*
     * Load one itinerary with its items and places.
     * Images will be loaded separately when accessed inside
     * the transactional service method.
     */
    @EntityGraph(attributePaths = {
            "traveller",
            "items",
            "items.place"
    })
    Optional<Itinerary>
            findWithItemsById(
                    Long itineraryId
            );

    /*
     * Optional compatibility query.
     */
    @EntityGraph(attributePaths = {
            "traveller",
            "items",
            "items.place"
    })
    List<Itinerary>
            findByTravellerEmailIgnoreCaseOrderByCreatedAtDesc(
                    String email
            );
}