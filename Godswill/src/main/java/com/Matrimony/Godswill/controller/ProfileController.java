package com.Matrimony.Godswill.controller;

import com.Matrimony.Godswill.model.Profile;
import com.Matrimony.Godswill.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profiles")
    public String listProfiles(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String religion,
            @RequestParam(required = false) Boolean verified,
            @RequestParam(required = false) Integer ageFrom,
            @RequestParam(required = false) Integer ageTo,
            @RequestParam(required = false) String maritalStatus,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String q,
            Model model) {

        List<Profile> profiles;

        // Search query takes priority
        if (q != null && !q.isEmpty()) {
            profiles = profileService.searchProfiles(q);
        }
        // Apply filters
        else if (gender != null || religion != null || verified != null ||
                ageFrom != null || ageTo != null || maritalStatus != null || education != null) {
            profiles = profileService.filterProfiles(gender, religion, verified,
                    ageFrom, ageTo, maritalStatus, education);
        }
        // Default: all profiles
        else {
            profiles = profileService.getAllProfiles();
        }

        model.addAttribute("profiles", profiles);
        model.addAttribute("activePage", "profiles");
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);

        return "profile-listing";
    }

    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable String id, Model model) {
        Optional<Profile> profile = profileService.getProfileById(id);

        if (profile.isEmpty()) {
            return "redirect:/profiles";
        }

        // Get similar profiles (same gender and religion)
        List<Profile> similarProfiles = profileService.filterProfiles(
                profile.get().getGender(),
                profile.get().getReligion(),
                null, null, null, null, null
        );

        // Remove current profile from similar profiles
        similarProfiles.removeIf(p -> p.getId().equals(id));

        // Limit to 3 similar profiles
        if (similarProfiles.size() > 3) {
            similarProfiles = similarProfiles.subList(0, 3);
        }

        model.addAttribute("profile", profile.get());
        model.addAttribute("similarProfiles", similarProfiles);
        model.addAttribute("canViewContact", false); // Set based on user session

        return "profile-details";
    }

    @GetMapping("/profile/create")
    public String showCreateProfileForm(Model model) {
        model.addAttribute("profile", new Profile());
        return "profile-create";
    }

    @PostMapping("/profile/create")
    public String createProfile(@ModelAttribute Profile profile,
                                RedirectAttributes redirectAttributes) {
        try {
            profileService.createProfile(profile);
            redirectAttributes.addFlashAttribute("success", "Profile created successfully!");
            return "redirect:/profiles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create profile. Please try again.");
            return "redirect:/profile/create";
        }
    }
}