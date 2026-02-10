package com.Matrimony.Godswill.repository;

import com.Matrimony.Godswill.model.SuccessStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuccessStoryRepository extends JpaRepository<SuccessStory, Long> {

    List<SuccessStory> findAllByOrderByMarriageDateDesc();

    List<SuccessStory> findByLocation(String location);
}