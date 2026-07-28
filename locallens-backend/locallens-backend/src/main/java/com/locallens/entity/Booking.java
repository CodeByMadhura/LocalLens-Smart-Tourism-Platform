package com.locallens.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.locallens.enums.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "number_of_people", nullable = false)
    private Integer numberOfPeople;

    @Column(
        name = "total_amount",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BookingStatus status = BookingStatus.PENDING;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "traveller_id",
        nullable = false
    )
    private User traveller;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "experience_package_id",
        nullable = false
    )
    private ExperiencePackage experiencePackage;
}