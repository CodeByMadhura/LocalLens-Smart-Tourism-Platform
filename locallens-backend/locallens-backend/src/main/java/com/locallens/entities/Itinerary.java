package com.locallens.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.locallens.enums.TravelType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "itineraries")
public class Itinerary extends BaseEntity {

    @Column(
        name = "title",
        nullable = false,
        length = 150
    )
    private String title;

    @Column(
        name = "destination",
        nullable = false,
        length = 100
    )
    private String destination;

    @Column(
        name = "number_of_days",
        nullable = false
    )
    private Integer numberOfDays;

    @Column(
        name = "budget",
        precision = 10,
        scale = 2
    )
    private BigDecimal budget;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "travel_type",
        length = 30
    )
    private TravelType travelType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(
        name = "interests",
        columnDefinition = "TEXT"
    )
    private String interests;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "traveller_id",
        nullable = false
    )
    private User traveller;

    @OneToMany(
        mappedBy = "itinerary",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @OrderBy("dayNumber ASC, displayOrder ASC")
    private List<ItineraryItem> items = new ArrayList<>();

    public void addItem(ItineraryItem item) {
        if (item == null) {
            return;
        }

        items.add(item);
        item.setItinerary(this);
    }

    public void removeItem(ItineraryItem item) {
        if (item == null) {
            return;
        }

        items.remove(item);
        item.setItinerary(null);
    }
}