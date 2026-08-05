package com.locallens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.locallens.entities.PlaceImage;

@Repository
public interface PlaceImageRepository
        extends JpaRepository<PlaceImage, Long> {

    List<PlaceImage> findByPlaceIdOrderByPrimaryImageDescIdAsc(
            Long placeId
    );

    Optional<PlaceImage> findByIdAndPlaceId(
            Long imageId,
            Long placeId
    );

    Optional<PlaceImage> findByPlaceIdAndPrimaryImageTrue(
            Long placeId
    );

    long countByPlaceId(Long placeId);

    void deleteByPlaceId(Long placeId);
}