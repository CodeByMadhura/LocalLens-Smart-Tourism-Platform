package com.locallens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.locallens.entities.UserPhoto;

@Repository
public interface UserPhotoRepository
        extends JpaRepository<UserPhoto, Long> {

    Optional<UserPhoto>
        findByUserIdAndProfilePhotoTrue(
            Long userId
        );

    List<UserPhoto> findByUserId(
        Long userId
    );

    void deleteByUserIdAndProfilePhotoTrue(
        Long userId
    );
}