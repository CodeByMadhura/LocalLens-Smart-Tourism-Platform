package com.locallens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.locallens.entities.UserAddress;
import com.locallens.enums.AddressType;

@Repository
public interface UserAddressRepository
        extends JpaRepository<UserAddress, Long> {

    List<UserAddress> findByUserId(
            Long userId
    );

    Optional<UserAddress> findFirstByUserIdAndCurrentTrue(
            Long userId
    );

    Optional<UserAddress> findFirstByUserIdAndAddressType(
            Long userId,
            AddressType addressType
    );

    void deleteByUserIdAndAddressType(
            Long userId,
            AddressType addressType
    );

    boolean existsByUserIdAndAddressType(
            Long userId,
            AddressType addressType
    );
}