package com.locallens.entity;

import java.time.LocalDate;

import com.locallens.enums.RequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "guide_requests")
public class GuideRequest extends BaseEntity{
	
	@Column(name = "travel_date")
	private LocalDate travelDate;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String message;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private RequestStatus status = RequestStatus.PENDING;
	
	@Enumerated(EnumType.STRING)
	@JoinColumn(name = "traveller_id", nullable = false)
	private User traveller;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "guide_id", nullable = false)
	private User guide;
	
	@ManyToOne
	@JoinColumn(name = "place_id")
	private Place place;

}
