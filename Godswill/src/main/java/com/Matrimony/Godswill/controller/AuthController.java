package com.Matrimony.Godswill.controller;

import com.Matrimony.Godswill.model.User;
import com.Matrimony.Godswill.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

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
            // Validate password match
            if (!user.getPassword().equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/register";
            }

            // Check if email already exists
            if (userService.existsByEmail(user.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email already registered!");
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/register";
            }

            // Check if phone already exists
            if (userService.existsByPhone(user.getPhone())) {
                redirectAttributes.addFlashAttribute("error", "Phone number already registered!");
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/register";
            }

            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Please login to continue.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed. Please try again.");
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/register";
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
            session.setAttribute("user", user.get());
            redirectAttributes.addFlashAttribute("success", "Login successful!");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email/phone or password!");
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Logged out successfully!");
        return "redirect:/";
    }
}