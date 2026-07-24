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
@Table(name = "places")
public class Place extends BaseEntity{
	
	@Column(nullable = false, length = 150)
	private String name;
	
	@Column(nullable = false, length = 100)
	private String city;
	
	@Column(length = 100)
	private String state;
	
	@Column(nullable = false, length = 50)
	private String category;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "food_description", columnDefinition = "TEXT")
	private String foodDescription;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal estimatedCost;
	
	@Column(precision = 10, scale = 7, nullable = false)
	private BigDecimal latitude;
	
	@Column(precision = 10, scale = 7, nullable = false)
	private BigDecimal longitude;
	
	@Column(name = "average_rating", precision = 3, scale = 2)
	private BigDecimal averageRating = BigDecimal.ZERO;
	
	@Column(name = "review_count", nullable = false)
	private Integer reviewCount = 0;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private ApprovalStatus status = ApprovalStatus.PENDING;
	
	@Column(name = "rejection_reason")
	private String rejectionReason;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "created_by_user_id", nullable = false)
	private User createdBy;
	
	@ManyToOne
	@JoinColumn(name = "approved_by_admin_id")
	private User approvedBy;
	
	@OneToMany(mappedBy = "place")
	private List<PlaceImage> images = new ArrayList<>();
	
	@OneToMany(mappedBy = "place")
	private List<Review> reviews = new ArrayList<>();
	
	@OneToMany(mappedBy = "place")
	private List<Favourite> favourites = new ArrayList<>();
	

}
