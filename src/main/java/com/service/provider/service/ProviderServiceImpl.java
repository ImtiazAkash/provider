package com.service.provider.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.service.provider.dto.ReviewDto;
import com.service.provider.model.Document;
import com.service.provider.model.Language;
import com.service.provider.model.Provider;
import com.service.provider.model.Review;
import com.service.provider.repository.DocumentsRepository;
import com.service.provider.repository.LanguageRepository;
import com.service.provider.repository.ProviderRepository;
import com.service.provider.repository.ReviewRepository;

import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private DocumentsRepository documentRepository;
    @Autowired
    private LanguageRepository languageRepository;

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @Override
    public void addProviderWithReviewAndDocument(String providerName, String bio, String email, String phone,
            String city, String country, MultipartFile imageFile, String reviewText, Date reviewDate,
            String reviewCountry, int rating, MultipartFile reviewImage, MultipartFile documentFile) {

        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String providerImagePath = "";
        if (imageFile != null && !imageFile.isEmpty()) {
            String profileImageFileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path imagePath = Paths.get(UPLOAD_DIRECTORY, profileImageFileName);
            try {
                Files.write(imagePath, imageFile.getBytes());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            providerImagePath = imagePath.toString();
        }

        Provider provider = new Provider();
        provider.setProviderName(providerName);
        provider.setBio(bio);
        provider.setEmail(email);
        provider.setPhone(phone);
        provider.setCity(city);
        provider.setCountry(country);
        provider.setImageFilePath(providerImagePath);
        provider = providerRepository.save(provider);

        // Save the provider first

        // Create and save the review
        String reviewImagePathString = "";
        if (reviewImage != null && !reviewImage.isEmpty()) {
            String profileImageFileName = System.currentTimeMillis() + "_" + reviewImage.getOriginalFilename();
            Path reviewImagePath = Paths.get(UPLOAD_DIRECTORY, profileImageFileName);
            try {
                Files.write(reviewImagePath, reviewImage.getBytes());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            reviewImagePathString = reviewImagePath.toString();
        }

        Review review = new Review();
        review.setReview(reviewText);
        review.setReviewDate(reviewDate);
        review.setReviewCountry(reviewCountry);
        review.setRating(rating);
        review.setReviewImagePath(reviewImagePathString);
        review.setProvider(provider);

        reviewRepository.save(review);
        // Create and save the document
        String documentFilePathString = "";
        if (documentFile != null && !documentFile.isEmpty()) {
            String profileImageFileName = System.currentTimeMillis() + "_" + documentFile.getOriginalFilename();
            Path documentFilePath = Paths.get(UPLOAD_DIRECTORY, profileImageFileName);
            try {
                Files.write(documentFilePath, documentFile.getBytes());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            documentFilePathString = documentFilePath.toString();
        }
        Document document = new Document();
        document.setDocumentFilePath(documentFilePathString);
        document.setProvider(provider);

        documentRepository.save(document);
    }

    @Override
    public Optional<Provider> getProviderById(long providerId) {
        return this.providerRepository.findById(providerId);

    }

    @Override
    public List<Provider> getAllProvider() {
        return this.providerRepository.findAll();
    }

    @Override
    public String addLanguagesToProvider(long providerId, Set<String> languageNames) {
        try {
            Provider provider = providerRepository.findById(providerId)
                    .orElseThrow(() -> new Exception("Provider not found with id " + providerId));

            // Fetch or create languages
            Set<Language> languages = new HashSet<>();
            for (String languageName : languageNames) {
                Language language = languageRepository.findByLanguage(languageName);
                if (language == null) {
                    language = new Language();
                    language.setLanguage(languageName);
                    language = languageRepository.save(language);
                }
                languages.add(language);
            }

            // Add languages to provider
            provider.getLanguages().addAll(languages);

            // Save the updated provider
            providerRepository.save(provider);
            return "Language added successfully";
        } catch (Exception e) {
            // TODO: handle exception
            return "Failed to add language!";
        }
    }

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
            for (ReviewDto newReview : newReviews) {
                Review review = new Review();
                review.setReview(newReview.getReview());
                review.setReviewDate(newReview.getReviewDate());
                review.setReviewCountry(newReview.getReviewCountry());
                review.setRating(newReview.getRating());
                review.setProvider(provider);

                String reviewImagePathString = "";
                if (newReview.getReviewImage() != null && !newReview.getReviewImage().isEmpty()) {
                    String profileImageFileName = System.currentTimeMillis() + "_"
                            + newReview.getReviewImage().getOriginalFilename();
                    Path reviewImagePath = Paths.get(UPLOAD_DIRECTORY, profileImageFileName);
                    try {
                        Files.write(reviewImagePath, newReview.getReviewImage().getBytes());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    reviewImagePathString = reviewImagePath.toString();
                }
                review.setReviewImagePath(reviewImagePathString);
                provider.getReviews().add(review);
            }

            // Save the updated provider
            providerRepository.save(provider);
            return "Reviews updated successfully";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "Updating reviews failed";
        }
    }

}
