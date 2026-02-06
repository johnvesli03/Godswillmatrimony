package com.Matrimony.Godswill.util;

import java.security.SecureRandom;

public class OtpUtil {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generate 6-digit OTP
     */
    public static String generateOtp() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Validate OTP format
     */
    public static boolean isValidOtp(String otp) {
        return otp != null && otp.matches("\\d{6}");
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Validate phone format (10 digits)
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    /**
     * Mask phone for display
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) {
            return phone;
        }
        return phone.substring(0, 2) + "****" + phone.substring(phone.length() - 2);
    }

    /**
     * Mask email for display
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String localPart = parts[0];

        if (localPart.length() <= 2) {
            return "*" + parts[1];
        }

        return localPart.charAt(0) + "****" + "@" + parts[1];
    }
}