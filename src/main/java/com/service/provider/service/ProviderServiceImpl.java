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
import com.service.provider.dto.AwardDto;
import com.service.provider.dto.DocumentDto;
import com.service.provider.dto.ProviderDto;
import com.service.provider.dto.ReviewDto;
import com.service.provider.model.Award;
import com.service.provider.model.Document;
import com.service.provider.model.Language;
import com.service.provider.model.Provider;
import com.service.provider.model.Review;
import com.service.provider.repository.AwardRepository;
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
    @Autowired
    private AwardRepository awardRepository;

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
                    String reviewImageFileName = System.currentTimeMillis() + "_"
                            + newReview.getReviewImage().getOriginalFilename();
                    Path reviewImagePath = Paths.get(UPLOAD_DIRECTORY, reviewImageFileName);
                    try {
                        Files.write(reviewImagePath, newReview.getReviewImage().getBytes());
                    } catch (IOException e) {

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

            e.printStackTrace();
            return "Updating reviews failed";
        }
    }

    @Override
    public String replaceAwardsAndRecognition(long providerId, List<AwardDto> newAwards) {
        try {

            Provider provider = providerRepository.findById(providerId)
                    .orElseThrow(() -> new Exception("Provider not found with id " + providerId));

            // Delete all existing reviews
            Set<Long> awardIdsToDelete = provider.getAwards().stream().map(Award::getAwardId)
                    .collect(Collectors.toSet());

            if (!awardIdsToDelete.isEmpty()) {
                awardRepository.deleteAllById(awardIdsToDelete);
            }

            // Clear the existing reviews in memory
            provider.getAwards().clear();

            // Add new reviews
            for (AwardDto newAward : newAwards) {
                Award award = new Award();
                award.setAwardTitle(newAward.getAwardTitle());
                award.setAwardDescription(newAward.getAwardDescription());
                award.setAwardYear(newAward.getAwardYear());
                
                award.setProvider(provider);

                
                
                provider.getAwards().add(award);
            }

            // Save the updated provider
            providerRepository.save(provider);
            return "Awards updated successfully";
        } catch (Exception e) {

            e.printStackTrace();
            return "Updating awards failed";
        }
    }

    @Override
    public String replaceDocuments(long providerId, List<DocumentDto> newDocuments) {
        try {

            Provider provider = providerRepository.findById(providerId)
                    .orElseThrow(() -> new Exception("Provider not found with id " + providerId));

            // Delete all existing reviews
            Set<Long> documentIdsToDelete = provider.getDocuments().stream().map(Document::getDocumentId)
                    .collect(Collectors.toSet());

            if (!documentIdsToDelete.isEmpty()) {
                documentRepository.deleteAllById(documentIdsToDelete);
            }

            // Clear the existing reviews in memory
            provider.getDocuments().clear();

            // Add new reviews
            for (DocumentDto newDocument : newDocuments) {
                Document document = new Document();
                document.setProvider(provider);

                String documentFilePathString = "";
                if (newDocument.getDocumentFile() != null && !newDocument.getDocumentFile().isEmpty()) {
                    String documentFileName = System.currentTimeMillis() + "_"
                            + newDocument.getDocumentFile().getOriginalFilename();
                    Path documentFilePath = Paths.get(UPLOAD_DIRECTORY, documentFileName);
                    try {
                        Files.write(documentFilePath, newDocument.getDocumentFile().getBytes());
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    documentFilePathString = documentFilePath.toString();
                }

                document.setDocumentFilePath(documentFilePathString);

                provider.getDocuments().add(document);
            }

            // Save the updated provider
            providerRepository.save(provider);
            return "Document updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Updating document failed";
        }
    }

    @Override
    public String updateProviderProfile(long providerId, ProviderDto profile) {
        try {
            Provider provider = providerRepository.findById(providerId)
                    .orElseThrow(() -> new Exception("Provider not found with id " + providerId));

            provider.setProviderName(profile.getProviderName());
            provider.setBio(profile.getBio());
            provider.setEmail(profile.getEmail());
            provider.setPhone(profile.getPhone());
            provider.setCity(profile.getCity());
            provider.setCountry(profile.getCountry());

            String providerImagePath = "";
            if (profile.getImageFile() != null && !profile.getImageFile().isEmpty()) {
                String profileImageFileName =
                        System.currentTimeMillis() + "_" + profile.getImageFile().getOriginalFilename();
                Path imagePath = Paths.get(UPLOAD_DIRECTORY, profileImageFileName);
                try {
                    Files.write(imagePath, profile.getImageFile().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                providerImagePath = imagePath.toString();
            }
            provider.setImageFilePath(providerImagePath);

            providerRepository.save(provider);
            return "Profile updated successfully";
        } catch (Exception e) {
            return "Profile updating failed";
        }
    }


    

}
