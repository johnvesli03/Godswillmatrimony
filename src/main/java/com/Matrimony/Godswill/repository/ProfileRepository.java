package com.Matrimony.Godswill.repository;

import com.Matrimony.Godswill.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String> {

    List<Profile> findByGender(String gender);
    List<Profile> findByVerified(Boolean verified);
    List<Profile> findByActive(Boolean active);
    List<Profile> findAllByOrderByCreatedAtDesc();

    @Query("{ $or: [ " +
            "{ 'firstName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'lastName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'city': { $regex: ?0, $options: 'i' } }, " +
            "{ 'profession': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Profile> searchProfiles(String query);

    Optional<Profile> findByUserId(String userId);

}

