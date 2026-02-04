package com.Matrimony.Godswill.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

@Document(collection = "carousel_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarouselImage {

    @Id
    private String id;

    private String imageUrl;

    private String title;

    private String description;

    @Indexed
    private int displayOrder;

    private boolean active = true;

    private String objectPosition; // NEW FIELD

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Constructor without ID
    public CarouselImage(String imageUrl, String title, String description, int displayOrder, String objectPosition) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.displayOrder = displayOrder;
        this.objectPosition = objectPosition;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}