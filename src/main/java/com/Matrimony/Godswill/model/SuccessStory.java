package com.Matrimony.Godswill.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "success_stories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessStory {

    @Id
    private String id;

    private String coupleName;
    private String story;
    private String imageUrl;
    private LocalDate marriageDate;
    private String location;
    private LocalDateTime createdAt;

    // Pre-persist logic
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}