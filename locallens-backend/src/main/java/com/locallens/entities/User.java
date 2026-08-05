package com.locallens.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.locallens.enums.GenderType;
import com.locallens.enums.UserRole;
import com.locallens.enums.VerificationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

    @Enumerated(EnumType.STRING)
    @Column(
        name = "gender",
        length = 30
    )
    private GenderType gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @JsonIgnore
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

    @Column(
        name = "is_active",
        nullable = false
    )
    private boolean active = false;

    @Column(
        name = "email_verified",
        nullable = false
    )
    private boolean emailVerified = false;

    @Column(
        name = "phone_verified",
        nullable = false
    )
    private boolean phoneVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "verification_status",
        nullable = false,
        length = 30
    )
    private VerificationStatus verificationStatus =
            VerificationStatus.NOT_SUBMITTED;

    @Column(
        name = "profile_completed",
        nullable = false
    )
    private boolean profileCompleted = false;

    @JsonIgnore
    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<UserAddress> addresses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<UserPhoto> photos = new ArrayList<>();

    @JsonIgnore
    @OneToOne(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private UserDetailInfo detailInfo;

    @JsonIgnore
    @OneToOne(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private LocalGuideProfile localGuideProfile;

    /*
     * Normalizes common String values before insert/update.
     */
    @PrePersist
    @PreUpdate
    private void normalizeUserData() {

        if (firstName != null) {
            firstName = firstName.trim();
        }

        if (lastName != null) {
            lastName = lastName.trim();
        }

        if (email != null) {
            email = email.trim().toLowerCase();
        }

        if (phoneNumber != null) {
            phoneNumber = phoneNumber.trim();
        }
    }

    /*
     * Activates the account after successful email verification.
     */
    public void updateAccountStatus() {
        this.active = this.emailVerified;
    }

    /*
     * Checks whether this user can log in.
     */
    public boolean canLogin() {
        return this.active && this.emailVerified;
    }

    /*
     * Checks whether the logged-in user is a traveller.
     */
    public boolean isTraveller() {
        return UserRole.TRAVELLER.equals(this.role);
    }

    /*
     * Checks whether the logged-in user is a local guide.
     */
    public boolean isLocalGuide() {
        return UserRole.LOCAL_GUIDE.equals(this.role);
    }

    /*
     * Checks whether the logged-in user is an admin.
     */
    public boolean isAdmin() {
        return UserRole.ADMIN.equals(this.role);
    }

    /*
     * Returns first name and last name together.
     */
    public String getFullName() {

        String first = firstName == null
                ? ""
                : firstName.trim();

        String last = lastName == null
                ? ""
                : lastName.trim();

        return (first + " " + last).trim();
    }

    /*
     * Adds an address and maintains both sides
     * of the relationship.
     */
    public void addAddress(UserAddress address) {

        if (address == null) {
            return;
        }

        if (addresses == null) {
            addresses = new ArrayList<>();
        }

        if (!addresses.contains(address)) {
            addresses.add(address);
        }

        if (address.getUser() != this) {
            address.setUser(this);
        }
    }

    /*
     * Removes an address and clears its user relationship.
     */
    public void removeAddress(UserAddress address) {

        if (address == null || addresses == null) {
            return;
        }

        boolean removed = addresses.remove(address);

        if (removed && address.getUser() == this) {
            address.setUser(null);
        }
    }

    /*
     * Adds a photo and maintains both sides
     * of the relationship.
     */
    public void addPhoto(UserPhoto photo) {

        if (photo == null) {
            return;
        }

        if (photos == null) {
            photos = new ArrayList<>();
        }

        if (!photos.contains(photo)) {
            photos.add(photo);
        }

        if (photo.getUser() != this) {
            photo.setUser(this);
        }
    }

    /*
     * Removes a photo and clears its user relationship.
     */
    public void removePhoto(UserPhoto photo) {

        if (photo == null || photos == null) {
            return;
        }

        boolean removed = photos.remove(photo);

        if (removed && photo.getUser() == this) {
            photo.setUser(null);
        }
    }

    /*
     * Returns the photo marked as the profile photo.
     *
     * This assumes UserPhoto contains:
     *
     * private boolean profile;
     *
     * and Lombok generates:
     *
     * isProfile()
     */
    public UserPhoto getProfilePhoto() {

        if (photos == null || photos.isEmpty()) {
            return null;
        }

        return photos.stream()
                .filter(photo ->
                        photo != null && photo.isProfilePhoto()
                )
                .findFirst()
                .orElse(null);
    }

    /*
     * Sets common user detail information.
     */
    public void setDetailInfo(UserDetailInfo detailInfo) {

        if (this.detailInfo == detailInfo) {
            return;
        }

        UserDetailInfo oldDetailInfo = this.detailInfo;
        this.detailInfo = detailInfo;

        if (oldDetailInfo != null
                && oldDetailInfo.getUser() == this) {

            oldDetailInfo.setUser(null);
        }

        if (detailInfo != null
                && detailInfo.getUser() != this) {

            detailInfo.setUser(this);
        }
    }

    /*
     * Removes common user detail information.
     */
    public void removeDetailInfo() {

        if (this.detailInfo == null) {
            return;
        }

        UserDetailInfo oldDetailInfo = this.detailInfo;
        this.detailInfo = null;

        if (oldDetailInfo.getUser() == this) {
            oldDetailInfo.setUser(null);
        }
    }

    /*
     * Sets the local guide profile.
     */
    public void setLocalGuideProfile(
            LocalGuideProfile localGuideProfile
    ) {

        if (this.localGuideProfile == localGuideProfile) {
            return;
        }

        LocalGuideProfile oldProfile =
                this.localGuideProfile;

        this.localGuideProfile = localGuideProfile;

        if (oldProfile != null
                && oldProfile.getUser() == this) {

            oldProfile.setUser(null);
        }

        if (localGuideProfile != null
                && localGuideProfile.getUser() != this) {

            localGuideProfile.setUser(this);
        }
    }

    /*
     * Removes the local guide profile relationship.
     */
    public void removeLocalGuideProfile() {

        if (this.localGuideProfile == null) {
            return;
        }

        LocalGuideProfile oldProfile =
                this.localGuideProfile;

        this.localGuideProfile = null;

        if (oldProfile.getUser() == this) {
            oldProfile.setUser(null);
        }
    }
}