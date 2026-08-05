package com.locallens.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.locallens.enums.ApprovalStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "places")
public class Place extends BaseEntity {

    @Column(
            name = "name",
            nullable = false,
            length = 150
    )
    private String name;

    @Column(
            name = "city",
            nullable = false,
            length = 100
    )
    private String city;

    @Column(
            name = "state",
            length = 100
    )
    private String state;

    @Column(
            name = "category",
            nullable = false,
            length = 50
    )
    private String category;

    @Column(
            name = "estimated_cost",
            precision = 10,
            scale = 2
    )
    private BigDecimal estimatedCost;

    @Column(
            name = "latitude",
            precision = 10,
            scale = 7,
            nullable = false
    )
    private BigDecimal latitude;

    @Column(
            name = "longitude",
            precision = 10,
            scale = 7,
            nullable = false
    )
    private BigDecimal longitude;

    @Column(
            name = "average_rating",
            precision = 3,
            scale = 2,
            nullable = false
    )
    private BigDecimal averageRating =
            BigDecimal.ZERO;

    @Column(
            name = "review_count",
            nullable = false
    )
    private Integer reviewCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private ApprovalStatus status =
            ApprovalStatus.PENDING;

    @Column(
            name = "rejection_reason",
            length = 500
    )
    private String rejectionReason;

    /*
     * The local guide who created this place.
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "created_by_user_id",
            nullable = false
    )
    private User createdBy;

    /*
     * The administrator who approved the place.
     * It remains null while the place is pending.
     */
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "approved_by_admin_id"
    )
    private User approvedBy;

    /*
     * Long text fields are stored in the
     * place_details table.
     *
     * One place has exactly one details record.
     */
    @OneToOne(
            mappedBy = "place",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private PlaceDetail details;

    /*
     * One place can have multiple images.
     */
    @OneToMany(
            mappedBy = "place",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy(
            "primaryImage DESC, id ASC"
    )
    private List<PlaceImage> images =
            new ArrayList<>();

    /*
     * One place can have multiple reviews.
     */
    @OneToMany(
            mappedBy = "place",
            fetch = FetchType.LAZY
    )
    private List<Review> reviews =
            new ArrayList<>();

    /*
     * One place can be saved as favourite
     * by multiple users.
     */
    @OneToMany(
            mappedBy = "place",
            fetch = FetchType.LAZY
    )
    private List<Favourite> favourites =
            new ArrayList<>();

    /*
     * Sets the PlaceDetail relationship on
     * both sides.
     */
    public void setDetails(
            PlaceDetail details
    ) {

        this.details = details;

        if (details != null) {
            details.setPlace(this);
        }
    }

    /*
     * Removes the details relationship from
     * both sides.
     */
    public void removeDetails() {

        if (this.details != null) {
            this.details.setPlace(null);
        }

        this.details = null;
    }

    /*
     * Adds an image and automatically sets
     * its place relationship.
     */
    public void addImage(
            PlaceImage image
    ) {

        if (image == null) {
            return;
        }

        if (images == null) {
            images =
                    new ArrayList<>();
        }

        images.add(image);
        image.setPlace(this);
    }

    /*
     * Removes an image from the place.
     */
    public void removeImage(
            PlaceImage image
    ) {

        if (image == null
                || images == null) {

            return;
        }

        images.remove(image);
        image.setPlace(null);
    }

    /*
     * Adds a review and sets its place.
     */
    public void addReview(
            Review review
    ) {

        if (review == null) {
            return;
        }

        if (reviews == null) {
            reviews =
                    new ArrayList<>();
        }

        reviews.add(review);
        review.setPlace(this);
    }

    /*
     * Removes a review from the place.
     */
    public void removeReview(
            Review review
    ) {

        if (review == null
                || reviews == null) {

            return;
        }

        reviews.remove(review);
        review.setPlace(null);
    }

    /*
     * Adds a favourite record.
     */
    public void addFavourite(
            Favourite favourite
    ) {

        if (favourite == null) {
            return;
        }

        if (favourites == null) {
            favourites =
                    new ArrayList<>();
        }

        favourites.add(favourite);
        favourite.setPlace(this);
    }

    /*
     * Removes a favourite record.
     */
    public void removeFavourite(
            Favourite favourite
    ) {

        if (favourite == null
                || favourites == null) {

            return;
        }

        favourites.remove(favourite);
        favourite.setPlace(null);
    }
}