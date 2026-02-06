package com.Matrimony.Godswill.service;

import com.Matrimony.Godswill.model.CarouselImage;
import com.Matrimony.Godswill.repository.CarouselImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarouselImageService {

    private final CarouselImageRepository carouselImageRepository;

    public List<CarouselImage> getAllActiveCarouselImages() {
        return carouselImageRepository.findByActiveTrueOrderByDisplayOrderAsc();
    }

    public List<CarouselImage> getAllCarouselImages() {
        return carouselImageRepository.findAll();
    }

    public CarouselImage getCarouselImageById(String id) {
        Optional<CarouselImage> image = carouselImageRepository.findById(id);
        return image.orElse(null);
    }

    public CarouselImage saveCarouselImage(CarouselImage image) {
        if (image.getId() == null) {
            image.setCreatedAt(LocalDateTime.now());
        }
        image.setUpdatedAt(LocalDateTime.now());
        return carouselImageRepository.save(image);
    }

    public CarouselImage updateCarouselImage(String id, CarouselImage imageDetails) {
        Optional<CarouselImage> existingImage = carouselImageRepository.findById(id);

        if (existingImage.isPresent()) {
            CarouselImage image = existingImage.get();
            if (imageDetails.getImageUrl() != null) {
                image.setImageUrl(imageDetails.getImageUrl());
            }
            if (imageDetails.getTitle() != null) {
                image.setTitle(imageDetails.getTitle());
            }
            if (imageDetails.getDescription() != null) {
                image.setDescription(imageDetails.getDescription());
            }
            image.setDisplayOrder(imageDetails.getDisplayOrder());
            image.setActive(imageDetails.isActive());
            image.setUpdatedAt(LocalDateTime.now());

            return carouselImageRepository.save(image);
        }
        return null;
    }

    public void deleteCarouselImage(String id) {
        carouselImageRepository.deleteById(id);
    }

    public void toggleCarouselImageActive(String id) {
        Optional<CarouselImage> image = carouselImageRepository.findById(id);
        if (image.isPresent()) {
            CarouselImage carouselImage = image.get();
            carouselImage.setActive(!carouselImage.isActive());
            carouselImage.setUpdatedAt(LocalDateTime.now());
            carouselImageRepository.save(carouselImage);
        }
    }
}