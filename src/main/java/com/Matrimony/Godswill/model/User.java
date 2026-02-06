package com.Matrimony.Godswill.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    @Indexed(unique = true)
    private String email;

    @NotBlank
    @Indexed(unique = true)
    private String phone;

    @NotBlank
    private String password;

    private String gender;

    private String dateOfBirth;

    // OTP Verification Status
    private Boolean emailVerified = false;
    private Boolean phoneVerified = false;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    // Pre-persist logic
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}