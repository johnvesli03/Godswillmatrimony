package com.Matrimony.Godswill.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "carousel_images",
        indexes = {
                @Index(name = "idx_carousel_display_order", columnList = "displayOrder")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarouselImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int displayOrder;

    private boolean active = true;

    private String objectPosition;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public CarouselImage(String imageUrl, String title, String description, int displayOrder, String objectPosition) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.displayOrder = displayOrder;
        this.objectPosition = objectPosition;
        this.active = true;
    }

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}