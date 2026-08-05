package com.locallens.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.locallens.entities.LocalGuideProfile;

public interface LocalGuideProfileRepository
        extends JpaRepository<LocalGuideProfile, Long> {

    Optional<LocalGuideProfile> findByUserId(
            Long userId
    );

    Optional<LocalGuideProfile> findByUserEmailIgnoreCase(
            String email
    );

    boolean existsByUserId(
            Long userId
    );
}