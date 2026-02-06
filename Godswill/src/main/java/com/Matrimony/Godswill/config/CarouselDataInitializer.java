package com.Matrimony.Godswill.config;

import com.Matrimony.Godswill.model.CarouselImage;
import com.Matrimony.Godswill.repository.CarouselImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarouselDataInitializer implements CommandLineRunner {

    private final CarouselImageRepository carouselImageRepository;

    @Override
    public void run(String... args) throws Exception {
        carouselImageRepository.deleteAll();

        System.out.println("🔄 Reinitializing carousel images...");

        // Image 1 - Show top (heads visible)
        carouselImageRepository.save(new CarouselImage(
                "/images/Carousel121.png",
                "Find Your Perfect Match",
                "Thousands of verified profiles waiting to connect with you",
                1,
                "center top"  // Heads at top
        ));

        // Image 2 - Show center (couple in middle)
        carouselImageRepository.save(new CarouselImage(
                "/images/carousel2.jpg",
                "Trusted & Secure Platform",
                "Safe and secure matrimonial platform for Indian families",
                2,
                "center"  // Center of image
        ));

        // Image 3 - Show top
        carouselImageRepository.save(new CarouselImage(
                "/images/carousel3.jpg",
                "Success Stories",
                "Join thousands of happy couples who found their soulmate",
                3,
                "center top"
        ));

        // Image 4 - Show bottom
        carouselImageRepository.save(new CarouselImage(
                "/images/carousel4.jpg",
                "Start Your Journey Today",
                "Register free and begin your search for the perfect partner",
                4,
                "center bottom"  // Bottom of image
        ));

        // Image 5 - Show center
        carouselImageRepository.save(new CarouselImage(
                "/images/Carousel11.jpg",
                "Real Connections",
                "Meet genuine people with shared values and interests",
                5,
                "center"
        ));

        // Image 6 - Show top
        carouselImageRepository.save(new CarouselImage(
                "/images/Carousel13.jpg",
                "Your Story Begins Here",
                "Find your destiny with our trusted matrimony platform",
                6,
                "center top"
        ));

        System.out.println("✅ Carousel images initialized successfully!");
    }
}