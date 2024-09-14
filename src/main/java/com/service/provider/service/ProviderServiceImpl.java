package com.service.provider.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.service.provider.model.Document;
import com.service.provider.model.Provider;
import com.service.provider.model.Review;
import com.service.provider.repository.DocumentsRepository;
import com.service.provider.repository.ProviderRepository;
import com.service.provider.repository.ReviewRepository;
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

}
