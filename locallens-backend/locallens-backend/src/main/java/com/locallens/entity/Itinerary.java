package com.locallens.entity;

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
public class Itinerary extends BaseEntity{
	
	@Column(nullable = false, length = 150)
	private String title;
	
	@Column(nullable = false, length = 100)
	private String destination;
	
	@Column(nullable = false)
	private Integer numberOfDays;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal budget;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "travel_type", length = 30)
	private TravelType travelType;
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(columnDefinition = "TEXT")
	private String interests;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "travller_id", nullable = false)
	private User traveller;
	
	@OneToMany(
			mappedBy = "itinerary",
			cascade = CascadeType.ALL,
			orphanRemoval = true
			)
	@OrderBy("dayNumber ASC, displayOrder ASC")
	private List<ItineraryItem> items = new ArrayList<>();
	

}
