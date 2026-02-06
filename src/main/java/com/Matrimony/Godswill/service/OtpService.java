package com.Matrimony.Godswill.service;

import com.Matrimony.Godswill.model.Otp;
import com.Matrimony.Godswill.repository.OtpRepository;
import com.Matrimony.Godswill.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final EmailService emailService;

    private static final int MAX_ATTEMPTS = 5;
    private static final String EMAIL_VERIFICATION = "EMAIL_VERIFICATION";

    public void sendEmailOtp(String email) {
        if (!OtpUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        otpRepository.deleteByEmailAndPurpose(email, EMAIL_VERIFICATION);

        String otpCode = OtpUtil.generateOtp();

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setPurpose(EMAIL_VERIFICATION);
        otp.setOtpCode(otpCode);
        otp.setAttemptCount(0);
        otp.onCreate();

        otpRepository.save(otp);

        emailService.sendOtpEmail(email, otpCode);
        System.out.println("✅ Email OTP sent to: " + OtpUtil.maskEmail(email));
    }

    public boolean verifyEmailOtp(String email, String otpCode) {
        if (!OtpUtil.isValidOtp(otpCode)) {
            throw new IllegalArgumentException("Invalid OTP format (must be 6 digits)");
        }

        Optional<Otp> otpOptional = otpRepository.findByEmailAndPurpose(email, EMAIL_VERIFICATION);

        if (otpOptional.isEmpty()) {
            throw new RuntimeException("OTP not found for this email");
        }

        Otp otp = otpOptional.get();

        if (otp.isExpired()) {
            otpRepository.delete(otp);
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }

        if (otp.getAttemptCount() >= MAX_ATTEMPTS) {
            otpRepository.delete(otp);
            throw new RuntimeException("Maximum OTP verification attempts exceeded. Please request a new OTP.");
        }

        if (!otp.getOtpCode().equals(otpCode)) {
            otp.setAttemptCount(otp.getAttemptCount() + 1);
            otpRepository.save(otp);
            int remaining = MAX_ATTEMPTS - otp.getAttemptCount();
            throw new RuntimeException("Invalid OTP. " + remaining + " attempts remaining.");
        }

        otp.setVerified(true);
        otpRepository.save(otp);

        System.out.println("✅ Email OTP verified for: " + email);
        return true;
    }

    public boolean isEmailVerified(String email) {
        Optional<Otp> otp = otpRepository.findByEmailAndPurpose(email, EMAIL_VERIFICATION);
        return otp.isPresent() && Boolean.TRUE.equals(otp.get().getVerified());
    }

    public void resendEmailOtp(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must be provided");
        }
        sendEmailOtp(email);
    }
}