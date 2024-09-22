package com.service.provider.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.service.provider.dto.ReviewDto;
import com.service.provider.model.Provider;
import com.service.provider.model.Review;
import com.service.provider.repository.ProviderRepository;
import com.service.provider.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

        @Autowired
    private ProviderRepository providerRepository;

    @Value("${project.image}")
    private String imageDir;

    @Override
    @Transactional
    public String replaceProviderReviews(Long providerId, List<ReviewDto> newReviews) {
        try {

            Provider provider = providerRepository.findById(providerId)
                    .orElseThrow(() -> new Exception("Provider not found with id " + providerId));

            // Delete all existing reviews
            Set<Long> reviewIdsToDelete = provider.getReviews().stream()
                    .map(Review::getReviewId)
                    .collect(Collectors.toSet());

            if (!reviewIdsToDelete.isEmpty()) {
                reviewRepository.deleteAllById(reviewIdsToDelete);
            }

            // Clear the existing reviews in memory
            provider.getReviews().clear();

            // Add new reviews
            System.out.println(newReviews);
            for (ReviewDto newReview : newReviews) {
                Review review = new Review();
                review.setReview(newReview.getReview());
                review.setReviewDate(newReview.getReviewDate());
                review.setReviewCountry(newReview.getReviewCountry());
                review.setRating(newReview.getRating());
                review.setProvider(provider);

                File uploadDir = new File(imageDir);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String reviewImagePathString = "";
                if (newReview.getReviewImage() != null && !newReview.getReviewImage().isEmpty()) {
                    String fileName = UUID.randomUUID().toString() + "_"
                            + newReview.getReviewImage().getOriginalFilename();
                    File destinationFile = new File(imageDir + fileName);
                    System.out.println("===================="+newReview.getReviewImage());
                    System.out.println("===================="+destinationFile);
                    try {
                        newReview.getReviewImage().transferTo(destinationFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    reviewImagePathString = fileName;
                } else {
                    reviewImagePathString = newReview.getReviewImagePath();
                }
                review.setReviewImagePath(reviewImagePathString);
                provider.getReviews().add(review);
            }

            // Save the updated provider
            providerRepository.save(provider);
            return "Reviews updated successfully";
        } catch (Exception e) {

            e.printStackTrace();
            return "Updating reviews failed";
        }
    }
}
