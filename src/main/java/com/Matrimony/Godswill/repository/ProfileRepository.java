package com.Matrimony.Godswill.repository;

import com.Matrimony.Godswill.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    List<Profile> findByGender(String gender);
    List<Profile> findByVerified(Boolean verified);
    List<Profile> findByActive(Boolean active);
    List<Profile> findAllByOrderByCreatedAtDesc();

    @Query("""
        SELECT p FROM Profile p
        WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(p.city) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(p.profession) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Profile> searchProfiles(@Param("query") String query);

    Optional<Profile> findByUserId(Long userId);
}