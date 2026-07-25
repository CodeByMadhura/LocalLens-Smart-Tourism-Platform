package com.locallens.entity;

import java.util.ArrayList;
import java.util.List;

import com.locallens.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity{
 
	@Column(nullable = false, length = 100)
	private String name;
	
	@Column(nullable = false, unique = true, length = 100)
	private String email;
	
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;
	
	@Column(nullable = false)
	private String phone;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private UserRole role;
	
	@Column(nullable = false)
	private boolean active = true;
	
	@OneToMany(mappedBy = "createdBy")
	private List<Place> submittedPlace = new ArrayList<>();
	
	@OneToMany(mappedBy = "user")
	private List<Review> reviews = new ArrayList<>();
	
	@OneToMany(mappedBy = "user")
	private List<Favourite> favourites = new ArrayList<>();
	
	@OneToMany(mappedBy = "traveller")
	private List<Itinerary> itineraries = new ArrayList<>();
	
	
}
