package com.locallens.entity;

import com.locallens.enums.AddressType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddress extends BaseEntity {

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;

    @Column(
        name = "address_line_1",
        nullable = false,
        length = 150
    )
    private String addressLine1;

    @Column(
        name = "address_line_2",
        length = 150
    )
    private String addressLine2;

    @Column(
        name = "address_line_3",
        length = 150
    )
    private String addressLine3;

    @Column(name = "area", length = 100)
    private String area;

    @Column(
        name = "city",
        nullable = false,
        length = 80
    )
    private String city;

    @Column(
        name = "state",
        nullable = false,
        length = 80
    )
    private String state;

    @Column(
        name = "postal_code",
        nullable = false,
        length = 10
    )
    private String postalCode;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "address_type",
        nullable = false,
        length = 20
    )
    private AddressType addressType;

    @Builder.Default
    @Column(name = "is_current", nullable = false)
    private boolean current = false;
}