package com.Matrimony.Godswill.service;

import com.Matrimony.Godswill.model.SuccessStory;
import com.Matrimony.Godswill.repository.SuccessStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuccessStoryService {

    private final SuccessStoryRepository successStoryRepository;

    public List<SuccessStory> getAllSuccessStories() {
        return successStoryRepository.findAllByOrderByMarriageDateDesc();
    }

    public SuccessStory createSuccessStory(SuccessStory story) {
        story.onCreate();
        return successStoryRepository.save(story);
    }

    public List<SuccessStory> getSuccessStoriesByLocation(String location) {
        return successStoryRepository.findByLocation(location);
    }
}