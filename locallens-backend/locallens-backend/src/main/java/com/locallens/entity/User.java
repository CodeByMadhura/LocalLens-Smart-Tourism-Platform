package com.locallens.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.locallens.enums.UserRole;
import com.locallens.enums.VerificationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_email",
            columnNames = "email"
        ),
        @UniqueConstraint(
            name = "uk_user_phone",
            columnNames = "phone_number"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(
        name = "first_name",
        nullable = false,
        length = 50
    )
    private String firstName;

    @Column(
        name = "last_name",
        nullable = false,
        length = 100
    )
    private String lastName;

    @Column(
        name = "email",
        nullable = false,
        length = 100
    )
    private String email;

    @Column(
        name = "phone_number",
        nullable = false,
        length = 15
    )
    private String phoneNumber;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(
        name = "password_hash",
        nullable = false,
        length = 255
    )
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "role",
        nullable = false,
        length = 30
    )
    private UserRole role;


    @Column(name = "is_active", nullable = false)
    private boolean active = false;


    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;


    @Column(name = "phone_verified", nullable = false)
    private boolean phoneVerified = false;


    @Enumerated(EnumType.STRING)
    @Column(
        name = "verification_status",
        nullable = false,
        length = 30
    )
    private VerificationStatus verificationStatus =
            VerificationStatus.PENDING;


    @Column(
        name = "profile_completed",
        nullable = false
    )
    private boolean profileCompleted = false;


    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<UserAddress> addresses =
            new ArrayList<>();


    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<UserPhoto> photos =
            new ArrayList<>();

    @OneToOne(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private UserDetailInfo detailInfo;

    public void updateAccountStatus() {
        this.active =
                this.emailVerified
                && this.phoneVerified;
    }

    public void addAddress(UserAddress address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(UserAddress address) {
        addresses.remove(address);
        address.setUser(null);
    }

    public void addPhoto(UserPhoto photo) {
        photos.add(photo);
        photo.setUser(this);
    }

    public void removePhoto(UserPhoto photo) {
        photos.remove(photo);
        photo.setUser(null);
    }

    public void setDetailInfo(UserDetailInfo detailInfo) {
        this.detailInfo = detailInfo;

        if (detailInfo != null) {
            detailInfo.setUser(this);
        }
    }
}