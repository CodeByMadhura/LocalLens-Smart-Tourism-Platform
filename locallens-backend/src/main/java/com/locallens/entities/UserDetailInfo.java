package com.locallens.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_detail_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailInfo extends BaseEntity {

    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true
    )
    private User user;

    @Column(
        name = "about_me",
        columnDefinition = "TEXT"
    )
    private String aboutMe;

    @Column(name = "family_member_count")
    private Integer familyMemberCount;

    @Column(
        name = "hobbies",
        length = 500
    )
    private String hobbies;

    @Column(
        name = "favourite_places",
        columnDefinition = "TEXT"
    )
    private String favouritePlaces;

    @Column(
        name = "instagram_profile",
        length = 255
    )
    private String instagramProfile;

    @Column(
        name = "facebook_profile",
        length = 255
    )
    private String facebookProfile;
}