package com.Matrimony.Godswill.service;

import com.Matrimony.Godswill.model.Profile;
import com.Matrimony.Godswill.model.User;
import com.Matrimony.Godswill.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final MongoTemplate mongoTemplate;

    // ✅ NEW: sequence generator for GWM-1, GWM-2, ...
    private final SequenceGeneratorService sequenceGeneratorService;

    private static final String PROFILE_SEQ = "profile_sequence";
    private static final String PROFILE_PREFIX = "GWM-";

    // ================= CREATE OR UPDATE PROFILE =================
    public Profile createProfile(Profile profile) {

        Optional<Profile> existing =
                profileRepository.findByUserId(profile.getUserId());

        if (existing.isPresent()) {
            // UPDATE EXISTING
            profile.setId(existing.get().getId());
            profile.onUpdate();
        } else {
            // CREATE NEW
            long next = sequenceGeneratorService.getNextSequence(PROFILE_SEQ);
            profile.setId(PROFILE_PREFIX + next); // ✅ GWM-1, GWM-2, ...

            profile.onCreate();
        }

        return profileRepository.save(profile);
    }

    // ================= UPDATE PROFILE =================
    public Profile updateProfile(Profile profile) {
        profile.onUpdate();
        return profileRepository.save(profile);
    }

    // ================= GET PROFILE BY ID =================
    public Optional<Profile> getProfileById(String id) {
        Optional<Profile> profile = profileRepository.findById(id);

        profile.ifPresent(p -> {
            if (p.getViewCount() == null) p.setViewCount(0);
            p.setViewCount(p.getViewCount() + 1);
            profileRepository.save(p);
        });

        return profile;
    }

    // ================= GET PROFILE BY USER ID =================
    public Optional<Profile> getProfileByUserId(String userId) {
        return profileRepository.findByUserId(userId);
    }

    // ================= GET ALL ACTIVE PROFILES =================
    public List<Profile> getAllProfiles() {
        return profileRepository.findByActive(true);
    }

    // ================= SEARCH =================
    public List<Profile> searchProfiles(String query) {
        return profileRepository.searchProfiles(query);
    }

    // ================= FILTER =================
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

        query.addCriteria(Criteria.where("active").is(true));

        return mongoTemplate.find(query, Profile.class);
    }

    // ================= AUTO CREATE AFTER REGISTRATION =================
    public Profile createProfileForUser(User user) {

        Optional<Profile> existing =
                profileRepository.findByUserId(user.getId());

        if (existing.isPresent()) {
            return existing.get(); // already exists
        }

        Profile profile = new Profile();

        // ✅ IMPORTANT: also assign the custom ID here, because this method creates a profile too
        long next = sequenceGeneratorService.getNextSequence(PROFILE_SEQ);
        profile.setId(PROFILE_PREFIX + next);

        profile.setUserId(user.getId());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setGender(user.getGender());
        profile.setEmail(user.getEmail());
        profile.setPhone(user.getPhone());

        if (user.getDateOfBirth() != null && !user.getDateOfBirth().isEmpty()) {
            profile.setDateOfBirth(LocalDate.parse(user.getDateOfBirth()));
            profile.setAge(
                    Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears()
            );
        }

        profile.setActive(true);
        profile.setVerified(false);
        profile.setViewCount(0);
        profile.setCountry("India");

        profile.onCreate();

        return profileRepository.save(profile);
    }
}