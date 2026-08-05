package com.locallens.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    name = "reviews",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_review_user_place",
            columnNames = {
                "user_id",
                "place_id"
            }
        )
    }
)
public class Review extends BaseEntity {

    @Column(
        name = "rating",
        nullable = false
    )
    private Integer rating;

    @Column(
        name = "comment",
        columnDefinition = "TEXT"
    )
    private String comment;

    @Column(
        name = "visible",
        nullable = false
    )
    private boolean visible = true;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "place_id",
        nullable = false
    )
    private Place place;
}