package com.locallens.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.locallens.enums.ApprovalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class ExperiencePackage extends BaseEntity{
	
	@Column(nullable = false, length = 150)
	private String title;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;
	
	@Column(name = "duration_hours")
	private Integer durationHours;
	
	@Column(name = "maximum_participants")
	private Integer maximumParticipants;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private ApprovalStatus status = ApprovalStatus.PENDING;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "guide_id", nullable = false)
	private User guide;
	
	@ManyToOne
	@JoinColumn(name = "place_id")
	private Place place;
	
	@OneToMany(mappedBy = "experiencePackage")
	private List<Booking> bookings = new ArrayList<>();
}
