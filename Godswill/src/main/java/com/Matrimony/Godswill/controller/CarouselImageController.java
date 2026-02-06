package com.Matrimony.Godswill.controller;

import com.Matrimony.Godswill.model.CarouselImage;
import com.Matrimony.Godswill.service.CarouselImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/carousel-images")
@RequiredArgsConstructor
public class CarouselImageController {

    private final CarouselImageService carouselImageService;

    @GetMapping
    public ResponseEntity<List<CarouselImage>> getAllCarouselImages() {
        return ResponseEntity.ok(carouselImageService.getAllCarouselImages());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CarouselImage>> getActiveCarouselImages() {
        return ResponseEntity.ok(carouselImageService.getAllActiveCarouselImages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarouselImage> getCarouselImageById(@PathVariable String id) {
        CarouselImage image = carouselImageService.getCarouselImageById(id);
        if (image != null) {
            return ResponseEntity.ok(image);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CarouselImage> createCarouselImage(@RequestBody CarouselImage image) {
        CarouselImage savedImage = carouselImageService.saveCarouselImage(image);
        return ResponseEntity.ok(savedImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarouselImage> updateCarouselImage(@PathVariable String id,
                                                             @RequestBody CarouselImage imageDetails) {
        CarouselImage updatedImage = carouselImageService.updateCarouselImage(id, imageDetails);
        if (updatedImage != null) {
            return ResponseEntity.ok(updatedImage);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarouselImage(@PathVariable String id) {
        carouselImageService.deleteCarouselImage(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<CarouselImage> toggleCarouselImageActive(@PathVariable String id) {
        carouselImageService.toggleCarouselImageActive(id);
        CarouselImage image = carouselImageService.getCarouselImageById(id);
        return ResponseEntity.ok(image);
    }
}