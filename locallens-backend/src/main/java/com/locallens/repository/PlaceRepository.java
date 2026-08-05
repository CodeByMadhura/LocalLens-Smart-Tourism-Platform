package com.locallens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.locallens.entities.Place;
import com.locallens.enums.ApprovalStatus;

@Repository
public interface PlaceRepository
        extends JpaRepository<Place, Long> {

    // =========================================================
    // LOCAL GUIDE QUERIES
    // =========================================================

    @EntityGraph(attributePaths = {
            "images",
            "createdBy",
            "details"
    })
    List<Place> findByCreatedByIdOrderByCreatedAtDesc(
            Long userId
    );

    @EntityGraph(attributePaths = {
            "images",
            "createdBy",
            "details"
    })
    List<Place>
            findByCreatedByIdAndStatusOrderByCreatedAtDesc(
                    Long userId,
                    ApprovalStatus status
            );

    boolean existsByIdAndCreatedById(
            Long placeId,
            Long userId
    );

    // =========================================================
    // ADMIN AND PUBLIC QUERIES
    // =========================================================

    /**
     * Used for PENDING, APPROVED and REJECTED places.
     */
    @EntityGraph(attributePaths = {
            "images",
            "createdBy",
            "details"
    })
    List<Place> findByStatusOrderByCreatedAtDesc(
            ApprovalStatus status
    );

    long countByStatus(
            ApprovalStatus status
    );

    @EntityGraph(attributePaths = {
            "images",
            "createdBy",
            "details"
    })
    List<Place> findTop5ByOrderByCreatedAtDesc();

    long countByCreatedById(
            Long userId
    );

    // =========================================================
    // TRAVELLER FILTER QUERIES
    // =========================================================

    @EntityGraph(attributePaths = {
            "images",
            "createdBy",
            "details"
    })
    List<Place>
            findByCityIgnoreCaseAndStatusOrderByCreatedAtDesc(
                    String city,
                    ApprovalStatus status
            );

    @EntityGraph(attributePaths = {
            "images",
            "createdBy",
            "details"
    })
    List<Place>
            findByCategoryIgnoreCaseAndStatusOrderByCreatedAtDesc(
                    String category,
                    ApprovalStatus status
            );

    // =========================================================
    // PLACE DETAILS QUERY
    // =========================================================

    /**
     * Loads a place with related images, creator and details.
     */
    @EntityGraph(attributePaths = {
            "images",
            "createdBy",
            "details"
    })
    Optional<Place> findWithImagesById(
            Long id
    );
}