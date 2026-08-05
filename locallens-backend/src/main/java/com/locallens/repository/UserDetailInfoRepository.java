package com.locallens.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.locallens.entities.UserDetailInfo;

public interface UserDetailInfoRepository
        extends JpaRepository<UserDetailInfo, Long> {

    Optional<UserDetailInfo> findByUserId(
            Long userId
    );
}