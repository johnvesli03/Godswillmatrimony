package com.Matrimony.Godswill.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    private String id;
    @Indexed(unique = true)
    private String userId;

    // Personal Information
    private String firstName;
    private String lastName;

    @Indexed
    private String gender;

    private LocalDate dateOfBirth;
    private Integer age;

    @Indexed
    private String maritalStatus;

    private Integer height;
    private String motherTongue;

    // Religious Information
    @Indexed
    private String religion;

    private String caste;

    // Location Details
    private String country;
    private String state;

    @Indexed
    private String city;

    private String pincode;

    // Professional Information
    @Indexed
    private String education;

    @Indexed
    private String profession;

    private String annualIncome;
    private String employedIn;

    // About
    private String aboutMe;

    // Contact Information
    @Indexed
    private String email;

    @Indexed
    private String phone;

    // Profile Image
    private String imageUrl;

    // Status
    @Indexed
    private Boolean verified = false;

    private Boolean active = true;

    private Boolean shortlisted = false;

    // Statistics
    private Integer viewCount = 0;
    private String lastActive = "Today";

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Helper method to get full name
    public String getName() {
        return firstName + " " + lastName;
    }

    // Helper method to get location
    public String getLocation() {
        return city + ", " + state;
    }

    // Explicit getter for shortlisted
    public Boolean getShortlisted() {
        return shortlisted != null ? shortlisted : false;
    }

    public void setShortlisted(Boolean shortlisted) {
        this.shortlisted = shortlisted;
    }

    // Pre-persist logic (called on create)
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (viewCount == null) {
            viewCount = 0;
        }
        if (verified == null) {
            verified = false;
        }
        if (active == null) {
            active = true;
        }
        if (shortlisted == null) {
            shortlisted = false;
        }
    }
    public Integer getCalculatedAge() {
        if (dateOfBirth == null) return null;
        return java.time.Period.between(dateOfBirth, java.time.LocalDate.now()).getYears();
    }

    // Pre-update logic (called on update)
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}