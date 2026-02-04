package com.Matrimony.Godswill.repository;

import com.Matrimony.Godswill.model.CarouselImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarouselImageRepository extends MongoRepository<CarouselImage, String> {

    List<CarouselImage> findByActiveTrueOrderByDisplayOrderAsc();

    List<CarouselImage> findByActiveTrue();

    CarouselImage findByDisplayOrder(int displayOrder);
}