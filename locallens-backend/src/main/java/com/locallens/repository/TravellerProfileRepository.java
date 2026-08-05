package com.locallens.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.locallens.entities.TravellerProfile;

@Repository
public interface TravellerProfileRepository
        extends JpaRepository<TravellerProfile, Long> {

    Optional<TravellerProfile> findByUserId(
            Long userId
    );

    boolean existsByUserId(
            Long userId
    );
}