package com.locallens.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.locallens.entities.PlaceDetail;

public interface PlaceDetailRepository
        extends JpaRepository<PlaceDetail, Long> {

    Optional<PlaceDetail> findByPlaceId(
            Long placeId
    );
}