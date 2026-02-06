package com.Matrimony.Godswill.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "otps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

    @Id
    private String id;

    @Indexed
    private String email;

    @Indexed
    private String phone;

    private String otpCode;

    @Indexed
    private String purpose; // "EMAIL_VERIFICATION" or "PHONE_VERIFICATION"

    private Boolean verified = false;

    private Integer attemptCount = 0;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        // OTP expires in 10 minutes
        if (expiresAt == null) {
            expiresAt = LocalDateTime.now().plusMinutes(10);
        }
    }
}