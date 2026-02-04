package com.Matrimony.Godswill.controller;

import com.Matrimony.Godswill.model.CarouselImage;
import com.Matrimony.Godswill.model.Profile;
import com.Matrimony.Godswill.model.SuccessStory;
import com.Matrimony.Godswill.service.CarouselImageService;
import com.Matrimony.Godswill.service.ProfileService;
import com.Matrimony.Godswill.service.SuccessStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProfileService profileService;
    private final SuccessStoryService successStoryService;
    private final CarouselImageService carouselImageService;

    @GetMapping("/")
    public String home(Model model) {
        try {
            // Get active carousel images
            List<CarouselImage> carouselImages = carouselImageService.getAllActiveCarouselImages();
            model.addAttribute("carouselImages", carouselImages);

            // Get all profiles
            List<Profile> profiles = profileService.getAllProfiles();

            // Limit to first 12 profiles for homepage
            if (profiles.size() > 12) {
                profiles = profiles.subList(0, 12);
            }
            model.addAttribute("profiles", profiles);

            // Get all success stories
            List<SuccessStory> successStories = successStoryService.getAllSuccessStories();

            // Limit to first 3 success stories for homepage
            if (successStories.size() > 3) {
                successStories = successStories.subList(0, 3);
            }
            model.addAttribute("successStories", successStories);

            model.addAttribute("activePage", "home");

        } catch (Exception e) {
            System.err.println("Error loading home page data: " + e.getMessage());
            e.printStackTrace();

            // Return empty lists if there's an error
            model.addAttribute("carouselImages", List.of());
            model.addAttribute("profiles", List.of());
            model.addAttribute("successStories", List.of());
            model.addAttribute("activePage", "home");
        }

        return "home";
    }
}