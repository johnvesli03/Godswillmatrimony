package com.Matrimony.Godswill.repository;

import com.Matrimony.Godswill.model.CarouselImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarouselImageRepository extends JpaRepository<CarouselImage, Long> {

    List<CarouselImage> findByActiveTrueOrderByDisplayOrderAsc();

    List<CarouselImage> findByActiveTrue();

    CarouselImage findByDisplayOrder(int displayOrder);
}