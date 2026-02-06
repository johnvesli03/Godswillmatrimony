package com.Matrimony.Godswill.repository;

import com.Matrimony.Godswill.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, String> {

    List<Profile> findByGender(String gender);

    List<Profile> findByReligion(String religion);

    List<Profile> findByVerified(Boolean verified);

    List<Profile> findByGenderAndReligion(String gender, String religion);

    List<Profile> findByActive(Boolean active);

    // Search profiles by name, profession, or location
    @Query("{ $or: [ " +
            "{ 'firstName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'lastName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'profession': { $regex: ?0, $options: 'i' } }, " +
            "{ 'city': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Profile> searchProfiles(String query);

    // Find by email
    Optional<Profile> findByEmail(String email);

    // Find by phone
    Optional<Profile> findByPhone(String phone);

    // Find profiles ordered by creation date
    List<Profile> findAllByOrderByCreatedAtDesc();
}