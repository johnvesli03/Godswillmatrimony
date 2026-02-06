package com.Matrimony.Godswill.controller;

import com.Matrimony.Godswill.model.SuccessStory;
import com.Matrimony.Godswill.service.SuccessStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SuccessStoryController {

    private final SuccessStoryService successStoryService;

    @GetMapping("/success-stories")
    public String successStories(Model model) {
        List<SuccessStory> stories = successStoryService.getAllSuccessStories();
        model.addAttribute("successStories", stories);
        model.addAttribute("activePage", "stories");
        return "success-stories";
    }
}