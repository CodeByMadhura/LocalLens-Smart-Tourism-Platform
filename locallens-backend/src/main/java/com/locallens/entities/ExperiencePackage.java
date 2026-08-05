package com.locallens.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.locallens.enums.ApprovalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "experience_packages")
public class ExperiencePackage extends BaseEntity {

    @Column(
        name = "title",
        nullable = false,
        length = 150
    )
    private String title;

    @Column(
        name = "description",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String description;

    @Column(
        name = "price",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal price;

    @Column(name = "duration_hours")
    private Integer durationHours;

    @Column(name = "maximum_participants")
    private Integer maximumParticipants;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false,
        length = 20
    )
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "guide_id",
        nullable = false
    )
    private User guide;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @OneToMany(
        mappedBy = "experiencePackage",
        fetch = FetchType.LAZY
    )
    private List<Booking> bookings = new ArrayList<>();

    public void addBooking(Booking booking) {
        if (booking == null) {
            return;
        }

        bookings.add(booking);
        booking.setExperiencePackage(this);
    }

    public void removeBooking(Booking booking) {
        if (booking == null) {
            return;
        }

        bookings.remove(booking);
        booking.setExperiencePackage(null);
    }
}