/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service.provider.controller;

import com.service.provider.model.Documents;
import com.service.provider.model.Profile;
import com.service.provider.model.Provider;
import com.service.provider.model.ProviderDto;
import com.service.provider.model.Review;
import com.service.provider.repository.ProviderRepository;
import com.service.provider.service.DocumentsService;
import com.service.provider.service.ProfileService;
import com.service.provider.service.ReviewService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author User
 */
@RestController
@CrossOrigin(origins = "*")
public class ProviderController {

    @Autowired
    private DocumentsService documentsService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private ProviderRepository prepo;

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @PostMapping("/saveprovider")
    public ResponseEntity<String> saveProvider(@ModelAttribute ProviderDto dto) throws IOException {
        try {
            // Ensure the directory exists (create if not)
            File uploadDir = new File(UPLOAD_DIRECTORY);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            Provider provider = new Provider();
            prepo.save(provider);
            
            long providerID = provider.getId();
            
             
            // Process Profile Data
            if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
                String profileImageFileName = System.currentTimeMillis() + "_" + dto.getImageFile().getOriginalFilename();
                Path imagePath = Paths.get(UPLOAD_DIRECTORY, profileImageFileName);
                Files.write(imagePath, dto.getImageFile().getBytes());

                // Update profile with image path
                Profile profile = new Profile();
                profile.setProviderid(providerID);
                profile.setProviderName(dto.getProviderName());
                profile.setBio(dto.getBio());
                profile.setEmail(dto.getEmail());
                profile.setPhone(dto.getPhone());
                profile.setCity(dto.getCity());
                profile.setCountry(dto.getCountry());
                profile.setImageFilePath(imagePath.toString());

                profileService.saveProfile(profile);
            }

            // Process Review Data
            if (dto.getReviewImage() != null && !dto.getReviewImage().isEmpty()) {
                String reviewImageFileName = System.currentTimeMillis() + "_" + dto.getReviewImage().getOriginalFilename();
                Path reviewImagePath = Paths.get(UPLOAD_DIRECTORY, reviewImageFileName);
                Files.write(reviewImagePath, dto.getReviewImage().getBytes());

                // Update review with image path
                Review review = new Review();
                                review.setProviderid(providerID);

                review.setRating(dto.getRating());
                review.setReview(dto.getReview());
                review.setReviewCountry(dto.getReviewCountry());
                review.setReviewDate(dto.getReviewDate());
                review.setReviewImagePath(reviewImagePath.toString());

                reviewService.saveReview(review);
            }

            // Process Document Files
            if (dto.getDocumentFile() != null && !dto.getDocumentFile().isEmpty()) {
                String documentFilename = System.currentTimeMillis() + "_" + dto.getDocumentFile().getOriginalFilename();
                Path documentFilePath = Paths.get(UPLOAD_DIRECTORY, documentFilename);
                Files.write(documentFilePath, dto.getDocumentFile().getBytes());

                Documents documents = new Documents();
                                documents.setProviderid(providerID);

                documents.setDocumentFilePath(documentFilePath.toString());

                documentsService.saveDocuments(documents);
            }

            return new ResponseEntity<>("Provider saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Saving provider failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getprovider") 
    public ResponseEntity<ProviderDto> getProvider(@RequestParam long providerID) {
        Profile profile = this.profileService.getProfileByProviderID(providerID);

        Review review = this.reviewService.findByProviderid(providerID);

        Documents documents = this.documentsService.findByProviderid(providerID);

        ProviderDto dto = new ProviderDto();
        dto.setProfileId(profile.getProfileId());
        dto.setProviderName(profile.getProviderName());
        dto.setBio(profile.getBio());
        dto.setCity(profile.getCity());
        dto.setCountry(profile.getCountry());
        dto.setEmail(profile.getEmail());
        dto.setPhone(profile.getPhone());
        dto.setProfileImageURL(profile.getImageFilePath());

        dto.setReviewId(review.getReviewId());
        dto.setReview(review.getReview());
        dto.setReviewDate(review.getReviewDate());
        dto.setReviewCountry(review.getReviewCountry());
        dto.setRating(review.getRating());
        dto.setReviewImageURL(review.getReviewImagePath());

        dto.setDocumentId(documents.getDocumentId());
        dto.setDocumentFile(null);
        dto.setDocumentFileURL(documents.getDocumentFilePath());
        
        return ResponseEntity.ok(dto);
        
    }
}
