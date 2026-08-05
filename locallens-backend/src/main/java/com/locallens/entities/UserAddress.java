package com.locallens.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.locallens.enums.AddressType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "user_addresses",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_address_type",
            columnNames = {
                "user_id",
                "address_type"
            }
        )
    },
    indexes = {
        @Index(
            name = "idx_user_addresses_user_id",
            columnList = "user_id"
        ),
        @Index(
            name = "idx_user_addresses_type",
            columnList = "address_type"
        )
    }
)
public class UserAddress extends BaseEntity {

    @Column(
        name = "address_line1",
        nullable = false,
        length = 150
    )
    private String addressLine1;

    @Column(
        name = "address_line2",
        length = 150
    )
    private String addressLine2;

    @Column(
        name = "address_line3",
        length = 150
    )
    private String addressLine3;

    @Column(
        name = "area",
        length = 100
    )
    private String area;

    @Column(
        name = "city",
        nullable = false,
        length = 100
    )
    private String city;

    @Column(
        name = "state",
        nullable = false,
        length = 100
    )
    private String state;

    @Column(
        name = "zip_code",
        length = 20
    )
    private String zipCode;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "address_type",
        nullable = false,
        length = 30
    )
    private AddressType addressType;

    /*
     * Only AddressType.CURRENT should normally have this set to true.
     */
    @Column(
        name = "is_current",
        nullable = false
    )
    private boolean current = false;

    @JsonIgnore
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;

    public boolean isCurrentAddress() {
        return AddressType.CURRENT.equals(addressType)
                || current;
    }

    public boolean isPermanentAddress() {
        return AddressType.PERMANENT.equals(addressType);
    }
}