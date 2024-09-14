/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service.provider.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.service.provider.dto.ReviewDto;
import com.service.provider.model.Provider;
import com.service.provider.model.Review;
import com.service.provider.model.ReviewList;
import com.service.provider.service.ProviderService;

/**
 *
 * @author User
 */
@RestController
@CrossOrigin(origins = "*")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @PostMapping("/saveprovider")
    public void createProvider(
        @RequestParam String providerName, @RequestParam String bio, @RequestParam String email, 
        @RequestParam String phone, @RequestParam String city, @RequestParam String country, 
        @RequestParam MultipartFile imageFile,
        @RequestParam String reviewText, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date reviewDate, @RequestParam String reviewCountry,
        @RequestParam int rating, @RequestParam MultipartFile reviewImage,
        @RequestParam MultipartFile documentFile) {

        providerService.addProviderWithReviewAndDocument(
            providerName, bio, email, phone, city, country, imageFile,
            reviewText, reviewDate, reviewCountry, rating, reviewImage,
            documentFile
        );
    }

    @GetMapping("/getProviderById")
    public ResponseEntity<Provider> getProviderById(@RequestParam long providerId) {
        Optional<Provider> provider = this.providerService.getProviderById(providerId);
        if (provider.isPresent()) {
            return new ResponseEntity<>(provider.get(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/retrieveAllProvider")
    public ResponseEntity<List<Provider>> getAllProvider() {
        return new ResponseEntity<>(this.providerService.getAllProvider(), HttpStatus.FOUND);
    }

     @PostMapping("/{providerId}/add-languages")
    public ResponseEntity<String> addLanguagesToProvider(
            @PathVariable Long providerId,
            @RequestBody Set<String> languageNames) {
        String provider = providerService.addLanguagesToProvider(providerId, languageNames);
        return ResponseEntity.ok(provider);
    }

    @PostMapping("/{providerId}/replace-reviews")
    public ResponseEntity<String> replaceProviderReviews(
            @PathVariable Long providerId,
            @ModelAttribute ReviewList list) {
                System.out.println(list);
        String provider = providerService.replaceProviderReviews(providerId, list.getReviews());
        return ResponseEntity.ok(provider);

    }
}
