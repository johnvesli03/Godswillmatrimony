package com.Matrimony.Godswill.repository;

import com.Matrimony.Godswill.model.SuccessStory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuccessStoryRepository extends MongoRepository<SuccessStory, String> {

    List<SuccessStory> findAllByOrderByMarriageDateDesc();

    List<SuccessStory> findByLocation(String location);
}