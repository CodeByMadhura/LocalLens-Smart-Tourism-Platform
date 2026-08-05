package com.locallens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.locallens.entities.GuideVerification;
import com.locallens.enums.VerificationStatus;

@Repository
public interface GuideVerificationRepository
        extends JpaRepository<GuideVerification, Long> {

    Optional<GuideVerification> findByGuideId(Long guideId);

    boolean existsByGuideId(Long guideId);

    List<GuideVerification> findByStatusOrderByCreatedAtAsc(
            VerificationStatus status
    );
}