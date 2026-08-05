package com.locallens.entities;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "itinerary_items")
public class ItineraryItem extends BaseEntity {

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "itinerary_id",
            nullable = false
    )
    private Itinerary itinerary;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "place_id",
            nullable = false
    )
    private Place place;

    @Column(
            name = "day_number",
            nullable = false
    )
    private Integer dayNumber;

    @Column(
            name = "display_order",
            nullable = false
    )
    private Integer displayOrder;

    @Column(
            name = "planned_time"
    )
    private LocalTime plannedTime;

    @Column(
            name = "notes",
            length = 1000
    )
    private String notes;
}