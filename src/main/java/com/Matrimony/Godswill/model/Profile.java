package com.Matrimony.Godswill.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "profiles",
        indexes = {
                @Index(name = "idx_profile_user_id", columnList = "userId", unique = true),
                @Index(name = "idx_profile_gender", columnList = "gender"),
                @Index(name = "idx_profile_marital_status", columnList = "maritalStatus"),
                @Index(name = "idx_profile_religion", columnList = "religion"),
                @Index(name = "idx_profile_city", columnList = "city"),
                @Index(name = "idx_profile_education", columnList = "education"),
                @Index(name = "idx_profile_profession", columnList = "profession"),
                @Index(name = "idx_profile_email", columnList = "email"),
                @Index(name = "idx_profile_phone", columnList = "phone"),
                @Index(name = "idx_profile_verified", columnList = "verified")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String profileCode; // e.g. GWM-1, GWM-2

    @Column(unique = true)
    private Long userId;

    private String firstName;
    private String lastName;

    private String gender;

    private LocalDate dateOfBirth;
    private Integer age;

    private String maritalStatus;

    private Integer height;
    private String motherTongue;

    private String religion;

    private String caste;

    private String country;
    private String state;

    private String city;

    private String pincode;

    private String education;

    private String profession;

    private String annualIncome;
    private String employedIn;

    @Column(columnDefinition = "TEXT")
    private String aboutMe;

    private String email;

    private String phone;

    private String imageUrl;

    private Boolean verified = false;

    private Boolean active = true;

    private Boolean shortlisted = false;

    private Integer viewCount = 0;
    private String lastActive = "Today";

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getLocation() {
        return city + ", " + state;
    }

    public Boolean getShortlisted() {
        return shortlisted != null ? shortlisted : false;
    }

    public void setShortlisted(Boolean shortlisted) {
        this.shortlisted = shortlisted;
    }

    @PrePersist
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

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}