package com.locallens.entities;

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
@Table(name = "place_images")
public class PlaceImage extends BaseEntity {

    @Column(
        name = "image_url",
        nullable = false,
        length = 500
    )
    private String imageUrl;

    @Column(
        name = "primary_image",
        nullable = false
    )
    private boolean primaryImage = false;

    @Column(
        name = "caption",
        length = 200
    )
    private String caption;

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