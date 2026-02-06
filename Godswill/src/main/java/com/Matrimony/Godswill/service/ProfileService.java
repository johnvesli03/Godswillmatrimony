package com.Matrimony.Godswill.service;

import com.Matrimony.Godswill.model.Profile;
import com.Matrimony.Godswill.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final MongoTemplate mongoTemplate;

    public Profile createProfile(Profile profile) {
        profile.onCreate();
        return profileRepository.save(profile);
    }

    public Optional<Profile> getProfileById(String id) {
        Optional<Profile> profile = profileRepository.findById(id);
        profile.ifPresent(p -> {
            p.setViewCount(p.getViewCount() + 1);
            profileRepository.save(p);
        });
        return profile;
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findByActive(true);
    }

    public List<Profile> getProfilesByGender(String gender) {
        return profileRepository.findByGender(gender);
    }

    public List<Profile> getVerifiedProfiles() {
        return profileRepository.findByVerified(true);
    }

    public List<Profile> searchProfiles(String query) {
        return profileRepository.searchProfiles(query);
    }

    public List<Profile> filterProfiles(String gender, String religion, Boolean verified,
                                        Integer ageFrom, Integer ageTo,
                                        String maritalStatus, String education) {
        Query query = new Query();

        if (gender != null && !gender.isEmpty()) {
            query.addCriteria(Criteria.where("gender").is(gender));
        }

        if (religion != null && !religion.isEmpty()) {
            query.addCriteria(Criteria.where("religion").is(religion));
        }

        if (verified != null) {
            query.addCriteria(Criteria.where("verified").is(verified));
        }

        if (ageFrom != null) {
            query.addCriteria(Criteria.where("age").gte(ageFrom));
        }

        if (ageTo != null) {
            query.addCriteria(Criteria.where("age").lte(ageTo));
        }

        if (maritalStatus != null && !maritalStatus.isEmpty()) {
            query.addCriteria(Criteria.where("maritalStatus").is(maritalStatus));
        }

        if (education != null && !education.isEmpty()) {
            query.addCriteria(Criteria.where("education").is(education));
        }

        // Only active profiles
        query.addCriteria(Criteria.where("active").is(true));

        return mongoTemplate.find(query, Profile.class);
    }

    public Profile updateProfile(Profile profile) {
        profile.onUpdate();
        return profileRepository.save(profile);
    }

    public void deleteProfile(String id) {
        profileRepository.deleteById(id);
    }

    public List<Profile> getRecentProfiles(int limit) {
        return profileRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .limit(limit)
                .toList();
    }
}