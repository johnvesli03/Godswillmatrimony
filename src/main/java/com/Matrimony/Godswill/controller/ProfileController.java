package com.Matrimony.Godswill.controller;

import com.Matrimony.Godswill.model.Profile;
import com.Matrimony.Godswill.model.User;
import com.Matrimony.Godswill.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Value("${app.upload.dir}")
    private String uploadDir;

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

        if (q != null && !q.isEmpty()) {
            profiles = profileService.searchProfiles(q);
        } else if (gender != null || religion != null || verified != null ||
                ageFrom != null || ageTo != null || maritalStatus != null || education != null) {
            profiles = profileService.filterProfiles(
                    gender, religion, verified, ageFrom, ageTo, maritalStatus, education
            );
        } else {
            profiles = profileService.getAllProfiles();
        }

        model.addAttribute("profiles", profiles);
        model.addAttribute("activePage", "profiles");
        return "profile-listing";
    }

    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable String id, Model model) {

        Optional<Profile> profile = profileService.getProfileById(id);
        if (profile.isEmpty()) return "redirect:/profiles";

        model.addAttribute("profile", profile.get());
        return "profile-details";
    }

    @GetMapping("/profile/create")
    public String showCreateProfileForm(Model model) {
        model.addAttribute("profile", new Profile());
        return "profile-create";
    }

    @PostMapping("/profile/create")
    public String createProfile(
            @ModelAttribute Profile profile,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            HttpSession session) throws Exception {

        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) return "redirect:/login";

        profile.setUserId(loggedInUser.getId());

        if (profileImage != null && !profileImage.isEmpty()) {

            String originalName = profileImage.getOriginalFilename();
            String safeOriginalName = (originalName == null) ? "image" : new File(originalName).getName();
            String fileName = System.currentTimeMillis() + "_" + safeOriginalName;

            File dir = new File(uploadDir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Could not create upload directory: " + dir.getAbsolutePath());
            }

            File dest = new File(dir, fileName);
            profileImage.transferTo(dest);

            profile.setImageUrl("/uploads/" + fileName);
        }

        profileService.createProfile(profile);
        return "redirect:/profile/my-profile";
    }

    @GetMapping("/profile/my-profile")
    public String viewMyProfile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) return "redirect:/login";

        Optional<Profile> profile = profileService.getProfileByUserId(loggedInUser.getId());
        if (profile.isEmpty()) return "redirect:/profile/create";

        model.addAttribute("profile", profile.get());
        model.addAttribute("isOwnProfile", true);
        return "user/user-profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please login first!");
            return "redirect:/login";
        }

        Optional<Profile> profile = profileService.getProfileByUserId(loggedInUser.getId());
        if (profile.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Profile not found!");
            return "redirect:/profile/create";
        }

        model.addAttribute("profile", profile.get());
        return "profile-create";
    }
}