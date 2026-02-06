package com.Matrimony.Godswill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Matrimony - Email Verification OTP");
            message.setText(buildOtpEmailBody(otp));

            mailSender.send(message);
            System.out.println("✅ OTP email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send OTP email: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public void sendWelcomeEmail(String toEmail, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to Matrimony!");
            message.setText(buildWelcomeEmailBody(firstName));

            mailSender.send(message);
            System.out.println("✅ Welcome email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send welcome email: " + e.getMessage());
        }
    }

    private String buildOtpEmailBody(String otp) {
        return "Hello,\n\n" +
                "Your Matrimony Email Verification OTP is:\n\n" +
                otp + "\n\n" +
                "This OTP will expire in 10 minutes.\n\n" +
                "If you didn't request this OTP, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Matrimony Team";
    }

    private String buildWelcomeEmailBody(String firstName) {
        return "Hello " + firstName + ",\n\n" +
                "Welcome to Matrimony!\n\n" +
                "Your account has been successfully created. " +
                "You can now start exploring profiles and finding your perfect life partner.\n\n" +
                "Thank you for joining our community.\n\n" +
                "Best regards,\n" +
                "Matrimony Team";
    }
}