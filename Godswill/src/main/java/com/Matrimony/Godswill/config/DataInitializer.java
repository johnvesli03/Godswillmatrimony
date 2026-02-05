package com.Matrimony.Godswill.config;

import com.Matrimony.Godswill.model.Profile;
import com.Matrimony.Godswill.model.SuccessStory;
import com.Matrimony.Godswill.repository.ProfileRepository;
import com.Matrimony.Godswill.repository.SuccessStoryRepository;
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

    @Override
    public void run(String... args) {

        profileRepository.deleteAll();
        successStoryRepository.deleteAll();

        createSampleProfiles();
        createSampleSuccessStories();

        System.out.println("✅ Sample data initialized successfully in MongoDB!");
        System.out.println("📊 Total Profiles: " + profileRepository.count());
        System.out.println("💑 Total Success Stories: " + successStoryRepository.count());
    }

    private void createSampleProfiles() {

        Profile profile1 = new Profile();
        profile1.setFirstName("Priya");
        profile1.setLastName("Sharma");
        profile1.setGender("female");
        profile1.setDateOfBirth(LocalDate.of(1995, 5, 15));
        profile1.setAge(28);
        profile1.setMaritalStatus("Never Married");
        profile1.setHeight(165);
        profile1.setMotherTongue("Hindi");
        profile1.setReligion("Hindu");
        profile1.setCountry("India");
        profile1.setState("Maharashtra");
        profile1.setCity("Mumbai");
        profile1.setEducation("Master's Degree");
        profile1.setProfession("Software Engineer");
        profile1.setAnnualIncome("₹10-15 Lakhs");
        profile1.setEmployedIn("Private Sector");
        profile1.setAboutMe("I am a simple, down-to-earth person who values family and relationships.");
        profile1.setEmail("priya.sharma@example.com");
        profile1.setPhone("+91 9876543210");
        profile1.setImageUrl("/images/profile1.jpg");
        profile1.setVerified(true);
        profile1.onCreate();

        Profile profile2 = new Profile();
        profile2.setFirstName("Rahul");
        profile2.setLastName("Patel");
        profile2.setGender("male");
        profile2.setDateOfBirth(LocalDate.of(1992, 8, 20));
        profile2.setAge(31);
        profile2.setMaritalStatus("Never Married");
        profile2.setHeight(175);
        profile2.setMotherTongue("Gujarati");
        profile2.setReligion("Hindu");
        profile2.setCountry("India");
        profile2.setState("Gujarat");
        profile2.setCity("Ahmedabad");
        profile2.setEducation("Bachelor's Degree");
        profile2.setProfession("Business Owner");
        profile2.setAnnualIncome("₹15+ Lakhs");
        profile2.setEmployedIn("Business");
        profile2.setAboutMe("Family-oriented person looking for a life partner.");
        profile2.setEmail("rahul.patel@example.com");
        profile2.setPhone("+91 9876543211");
        profile2.setImageUrl("/images/profile2.jpg");
        profile2.setVerified(true);
        profile2.onCreate();

        Profile profile3 = new Profile();
        profile3.setFirstName("Anjali");
        profile3.setLastName("Singh");
        profile3.setGender("female");
        profile3.setDateOfBirth(LocalDate.of(1996, 3, 10));
        profile3.setAge(27);
        profile3.setMaritalStatus("Never Married");
        profile3.setHeight(160);
        profile3.setMotherTongue("Hindi");
        profile3.setReligion("Hindu");
        profile3.setCountry("India");
        profile3.setState("Delhi");
        profile3.setCity("New Delhi");
        profile3.setEducation("Master's Degree");
        profile3.setProfession("Doctor");
        profile3.setAnnualIncome("₹10-15 Lakhs");
        profile3.setEmployedIn("Government");
        profile3.setAboutMe("Caring and compassionate medical professional.");
        profile3.setEmail("anjali.singh@example.com");
        profile3.setPhone("+91 9876543212");
        profile3.setImageUrl("/images/profile3.jpg");
        profile3.setVerified(true);
        profile3.onCreate();

        Profile profile4 = new Profile();
        profile4.setFirstName("Amit");
        profile4.setLastName("Kumar");
        profile4.setGender("male");
        profile4.setDateOfBirth(LocalDate.of(1990, 11, 25));
        profile4.setAge(33);
        profile4.setMaritalStatus("Never Married");
        profile4.setHeight(178);
        profile4.setMotherTongue("Hindi");
        profile4.setReligion("Hindu");
        profile4.setCountry("India");
        profile4.setState("Karnataka");
        profile4.setCity("Bangalore");
        profile4.setEducation("Master's Degree");
        profile4.setProfession("Data Scientist");
        profile4.setAnnualIncome("₹15+ Lakhs");
        profile4.setEmployedIn("Private Sector");
        profile4.setAboutMe("Tech enthusiast with traditional values.");
        profile4.setEmail("amit.kumar@example.com");
        profile4.setPhone("+91 9876543213");
        profile4.setImageUrl("/images/profile4.jpg");
        profile4.setVerified(false);
        profile4.onCreate();

        profileRepository.saveAll(
                Arrays.asList(profile1, profile2, profile3, profile4)
        );
    }

    private void createSampleSuccessStories() {

        SuccessStory story1 = new SuccessStory();
        story1.setCoupleName("Rahul & Priya");
        story1.setStory("We found each other through this wonderful platform.");
        story1.setMarriageDate(LocalDate.of(2024, 1, 15));
        story1.setLocation("Mumbai, India");
        story1.setImageUrl("/images/cpl_1.jpg");
        story1.onCreate();

        SuccessStory story2 = new SuccessStory();
        story2.setCoupleName("Amit & Sneha");
        story2.setStory("The platform made it easy to connect.");
        story2.setMarriageDate(LocalDate.of(2023, 11, 20));
        story2.setLocation("Bangalore, India");
        story2.setImageUrl("/images/cpl_2.jpg");
        story2.onCreate();

        successStoryRepository.saveAll(Arrays.asList(story1, story2));
    }
}
