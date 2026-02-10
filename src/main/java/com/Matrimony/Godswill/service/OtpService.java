package com.Matrimony.Godswill.service;

import com.Matrimony.Godswill.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final EmailService emailService;

    private static final int MAX_ATTEMPTS = 5;
    private static final int OTP_VALID_MINUTES = 5;

    // In-memory OTP store (email -> otp data)
    private final Map<String, OtpEntry> emailOtpStore = new ConcurrentHashMap<>();

    private static class OtpEntry {
        String otpCode;
        int attempts;
        LocalDateTime expiresAt;

        OtpEntry(String otpCode, int attempts, LocalDateTime expiresAt) {
            this.otpCode = otpCode;
            this.attempts = attempts;
            this.expiresAt = expiresAt;
        }
    }

    public void sendEmailOtp(String email) {
        if (!OtpUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        String otpCode = OtpUtil.generateOtp();

        emailOtpStore.put(email,
                new OtpEntry(otpCode, 0, LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES)));

        emailService.sendOtpEmail(email, otpCode);
        System.out.println("✅ Email OTP sent to: " + OtpUtil.maskEmail(email));
    }

    public boolean verifyEmailOtp(String email, String otpCode) {
        if (!OtpUtil.isValidOtp(otpCode)) {
            throw new IllegalArgumentException("Invalid OTP format (must be 6 digits)");
        }

        OtpEntry entry = Optional.ofNullable(emailOtpStore.get(email))
                .orElseThrow(() -> new RuntimeException("OTP not found for this email"));

        if (LocalDateTime.now().isAfter(entry.expiresAt)) {
            emailOtpStore.remove(email);
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }

        if (entry.attempts >= MAX_ATTEMPTS) {
            emailOtpStore.remove(email);
            throw new RuntimeException("Maximum OTP verification attempts exceeded. Please request a new OTP.");
        }

        if (!entry.otpCode.equals(otpCode)) {
            entry.attempts++;
            int remaining = MAX_ATTEMPTS - entry.attempts;
            throw new RuntimeException("Invalid OTP. " + remaining + " attempts remaining.");
        }

        // OTP is valid, remove it so it can't be reused
        emailOtpStore.remove(email);

        System.out.println("✅ Email OTP verified for: " + email);
        return true;
    }

    public void resendEmailOtp(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must be provided");
        }
        sendEmailOtp(email);
    }
}