package com.Matrimony.Godswill.service;

import com.Matrimony.Godswill.model.Profile;
import com.Matrimony.Godswill.model.User;
import com.Matrimony.Godswill.repository.ProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EntityManager entityManager;
    private final SequenceGeneratorService sequenceGeneratorService;

    private static final String PROFILE_SEQ = "profile_sequence";
    private static final String PROFILE_PREFIX = "GWM-";

    // ================= CREATE OR UPDATE PROFILE =================
    public Profile createProfile(Profile profile) {
        Optional<Profile> existing = profileRepository.findByUserId(profile.getUserId());

        if (existing.isPresent()) {
            profile.setId(existing.get().getId());
            profile.setProfileCode(existing.get().getProfileCode());
            profile.onUpdate();
        } else {
            long next = sequenceGeneratorService.getNextSequence(PROFILE_SEQ);
            profile.setProfileCode(PROFILE_PREFIX + next);
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
    public Optional<Profile> getProfileById(Long id) {
        Optional<Profile> profile = profileRepository.findById(id);

        profile.ifPresent(p -> {
            if (p.getViewCount() == null) p.setViewCount(0);
            p.setViewCount(p.getViewCount() + 1);
            profileRepository.save(p);
        });

        return profile;
    }

    // ================= GET PROFILE BY USER ID =================
    public Optional<Profile> getProfileByUserId(Long userId) {
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

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Profile> cq = cb.createQuery(Profile.class);
        Root<Profile> root = cq.from(Profile.class);

        List<Predicate> predicates = new ArrayList<>();

        if (gender != null && !gender.isEmpty()) {
            predicates.add(cb.equal(root.get("gender"), gender));
        }

        if (religion != null && !religion.isEmpty()) {
            predicates.add(cb.equal(root.get("religion"), religion));
        }

        if (verified != null) {
            predicates.add(cb.equal(root.get("verified"), verified));
        }

        if (ageFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("age"), ageFrom));
        }

        if (ageTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("age"), ageTo));
        }

        if (maritalStatus != null && !maritalStatus.isEmpty()) {
            predicates.add(cb.equal(root.get("maritalStatus"), maritalStatus));
        }

        if (education != null && !education.isEmpty()) {
            predicates.add(cb.equal(root.get("education"), education));
        }

        predicates.add(cb.equal(root.get("active"), true));

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Profile> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    // ================= AUTO CREATE AFTER REGISTRATION =================
    public Profile createProfileForUser(User user) {
        Optional<Profile> existing = profileRepository.findByUserId(user.getId());

        if (existing.isPresent()) {
            return existing.get();
        }

        Profile profile = new Profile();

        long next = sequenceGeneratorService.getNextSequence(PROFILE_SEQ);
        profile.setProfileCode(PROFILE_PREFIX + next);

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