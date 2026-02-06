package com.Matrimony.Godswill.controller;

import com.Matrimony.Godswill.model.User;
import com.Matrimony.Godswill.service.EmailService;
import com.Matrimony.Godswill.service.OtpService;
import com.Matrimony.Godswill.service.ProfileService;
import com.Matrimony.Godswill.service.UserService;
import com.Matrimony.Godswill.util.OtpUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final ProfileService profileService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam String confirmPassword,
                           RedirectAttributes redirectAttributes) {
        try {
            if (!user.getPassword().equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
                redirectAttributes.addFlashAttribute("templates/user", user);
                return "redirect:/register";
            }

            if (!OtpUtil.isValidEmail(user.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Invalid email format!");
                redirectAttributes.addFlashAttribute("templates/user", user);
                return "redirect:/register";
            }

            if (!OtpUtil.isValidPhone(user.getPhone())) {
                redirectAttributes.addFlashAttribute("error", "Phone number must be 10 digits!");
                redirectAttributes.addFlashAttribute("templates/user", user);
                return "redirect:/register";
            }

            if (userService.existsByEmail(user.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email already registered!");
                redirectAttributes.addFlashAttribute("templates/user", user);
                return "redirect:/register";
            }

            if (userService.existsByPhone(user.getPhone())) {
                redirectAttributes.addFlashAttribute("error", "Phone number already registered!");
                redirectAttributes.addFlashAttribute("templates/user", user);
                return "redirect:/register";
            }

            User savedUser = userService.registerUser(user);
            System.out.println("👤 User registered: " + savedUser.getId());

            // Send OTP to email ONLY
            System.out.println("📧 Sending email OTP...");
            otpService.sendEmailOtp(user.getEmail());

            // REMOVED: Send OTP to phone
            // System.out.println("📱 Sending SMS OTP...");
            // otpService.sendPhoneOtp(user.getPhone());

            redirectAttributes.addFlashAttribute("message", "✅ OTP sent to your email!");
            redirectAttributes.addFlashAttribute("userId", savedUser.getId());
            redirectAttributes.addFlashAttribute("email", user.getEmail());

            // REMOVED: phone masking since we are not validating SMS OTP
            // redirectAttributes.addFlashAttribute("phone", OtpUtil.maskPhone(user.getPhone()));

            return "redirect:/verify-otp";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Validation error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("templates/user", user);
            return "redirect:/register";
        } catch (Exception e) {
            System.err.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            redirectAttributes.addFlashAttribute("templates/user", user);
            return "redirect:/register";
        }
    }

    @GetMapping("/verify-otp")
    public String showOtpForm(Model model) {
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String userId,
                            @RequestParam String email,
                            @RequestParam String emailOtp,
                            RedirectAttributes redirectAttributes) {
        try {
            System.out.println("🔐 Verifying Email OTP...");

            // Verify email OTP ONLY
            otpService.verifyEmailOtp(email, emailOtp);
            System.out.println("✅ Email OTP verified");

            Optional<User> userOptional = userService.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setEmailVerified(true);

                // REMOVED: phone OTP verification + phoneVerified flag update
                // user.setPhoneVerified(true);

                userService.updateUser(user);

                profileService.createProfileForUser(user);
                System.out.println("✅ Profile created for user: " + user.getId());

                emailService.sendWelcomeEmail(email, user.getFirstName());

                System.out.println("✅ User verification complete: " + user.getId());
                redirectAttributes.addFlashAttribute("success",
                        "✅ Email verified! Your account is now active. You can login.");
                return "redirect:/login";
            }

            redirectAttributes.addFlashAttribute("error", "User not found!");
            return "redirect:/verify-otp";

        } catch (Exception e) {
            System.err.println("❌ OTP Verification error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "❌ " + e.getMessage());
            redirectAttributes.addFlashAttribute("userId", userId);
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/verify-otp";
        }
    }

    @PostMapping("/resend-otp")
    @ResponseBody
    public Map<String, String> resendOtp(@RequestParam(required = false) String email,
                                         @RequestParam(required = false) String phone) {
        try {
            // Keep ONLY email resend (optional but recommended)
            if (email != null && !email.isEmpty()) {
                otpService.sendEmailOtp(email);
                return Map.of("status", "success", "message", "✅ Email OTP resent");
            }

            // REMOVED: phone resend
            // else if (phone != null && !phone.isEmpty()) { ... }

            return Map.of("status", "error", "message", "❌ Email required");

        } catch (Exception e) {
            return Map.of("status", "error", "message", "❌ " + e.getMessage());
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        Optional<User> user = userService.login(email, password);

        if (user.isPresent()) {
            if (!user.get().getEmailVerified()) {
                redirectAttributes.addFlashAttribute("error",
                        "⚠️ Please verify your email before logging in!");
                redirectAttributes.addFlashAttribute("email", email);
                return "redirect:/login";
            }

            session.setAttribute("user", user.get());
            redirectAttributes.addFlashAttribute("success", "✅ Login successful!");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "❌ Invalid email or password!");
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "✅ Logged out successfully!");
        return "redirect:/";
    }
}