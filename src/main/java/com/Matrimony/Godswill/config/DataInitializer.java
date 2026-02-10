package com.Matrimony.Godswill.config;

import com.Matrimony.Godswill.model.Profile;
import com.Matrimony.Godswill.model.SuccessStory;
import com.Matrimony.Godswill.repository.ProfileRepository;
import com.Matrimony.Godswill.repository.SuccessStoryRepository;
import com.Matrimony.Godswill.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProfileRepository profileRepository;
    private final SuccessStoryRepository successStoryRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    private static final String PROFILE_SEQ = "profile_sequence";
    private static final String PROFILE_PREFIX = "GWM-";

    @Override
    public void run(String... args) {

        System.out.println("🌱 Checking DB before seeding...");

        if (profileRepository.count() == 0) {
            createSampleProfiles();
            System.out.println("✅ Sample Profiles Added");
        } else {
            System.out.println("⏭ Profiles already exist. Skipping...");
        }

        if (successStoryRepository.count() == 0) {
            createSampleSuccessStories();
            System.out.println("✅ Sample Success Stories Added");
        } else {
            System.out.println("⏭ Success Stories already exist. Skipping...");
        }

        System.out.println("📊 Total Profiles: " + profileRepository.count());
        System.out.println("💑 Total Success Stories: " + successStoryRepository.count());
    }

    private void createSampleProfiles() {

        Profile profile1 = new Profile();
        profile1.setProfileCode(PROFILE_PREFIX + sequenceGeneratorService.getNextSequence(PROFILE_SEQ));
        profile1.setFirstName("Elisa");
        profile1.setLastName("Hope");
        profile1.setGender("female");
        profile1.setDateOfBirth(LocalDate.of(1995, 5, 15));
        profile1.setAge(28);
        profile1.setMaritalStatus("Never Married");
        profile1.setHeight(165);
        profile1.setMotherTongue("Hindi");
        profile1.setReligion("Cristian");
        profile1.setCountry("India");
        profile1.setState("Maharashtra");
        profile1.setCity("Mumbai");
        profile1.setEducation("Master's Degree");
        profile1.setProfession("Software Engineer");
        profile1.setAnnualIncome("₹10-15 Lakhs");
        profile1.setEmployedIn("Private Sector");
        profile1.setAboutMe("Simple and family-oriented.");
        profile1.setEmail("priya.sharma@example.com");
        profile1.setPhone("+919876543210");
        profile1.setImageUrl("/uploads/girl_1.jpg");
        profile1.setVerified(true);
        profile1.onCreate();

        Profile profile2 = new Profile();
        profile2.setProfileCode(PROFILE_PREFIX + sequenceGeneratorService.getNextSequence(PROFILE_SEQ));
        profile2.setFirstName("Mathew");
        profile2.setLastName("Hai");
        profile2.setGender("male");
        profile2.setDateOfBirth(LocalDate.of(1992, 8, 20));
        profile2.setAge(31);
        profile2.setMaritalStatus("Never Married");
        profile2.setHeight(175);
        profile2.setMotherTongue("Gujarati");
        profile2.setReligion("Converted Cristian");
        profile2.setCountry("India");
        profile2.setState("Gujarat");
        profile2.setCity("Ahmedabad");
        profile2.setEducation("Bachelor's Degree");
        profile2.setProfession("Business Owner");
        profile2.setAnnualIncome("₹15+ Lakhs");
        profile2.setEmployedIn("Business");
        profile2.setAboutMe("Family-oriented person.");
        profile2.setEmail("rahul.patel@example.com");
        profile2.setPhone("+919876543211");
        profile2.setImageUrl("/uploads/boy_1.jpg");
        profile2.setVerified(true);
        profile2.onCreate();

        profileRepository.saveAll(Arrays.asList(profile1, profile2));
    }

    private void createSampleSuccessStories() {

        SuccessStory story1 = new SuccessStory();
        story1.setCoupleName("Rahul & Priya");
        story1.setStory("We found each other here!");
        story1.setMarriageDate(LocalDate.of(2024, 1, 15));
        story1.setLocation("Mumbai, India");
        story1.setImageUrl("/images/cpl_1.jpg");
        story1.onCreate();

        successStoryRepository.save(story1);
    }
}