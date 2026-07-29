package com.locallens.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
	name = "favourites",
	uniqueConstraints = {
			@UniqueConstraint(
					name = "uk_favourite_user_place",
					columnNames = {"user_id", "place_id"}
					)
	}
		)
public class Favourite extends BaseEntity{
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "place_id", nullable = false)
	private Place place;

}
