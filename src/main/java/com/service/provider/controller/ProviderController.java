/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service.provider.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.service.provider.dto.AwardDto;
import com.service.provider.dto.ProviderDto;
import com.service.provider.dto.ProviderResponse;
import com.service.provider.model.AwardList;
import com.service.provider.model.DocumentList;
import com.service.provider.model.Provider;
import com.service.provider.model.ReviewList;
import com.service.provider.service.LanguageService;
import com.service.provider.service.ProviderService;

/**
 *
 * @author User
 */
@RestController
public class ProviderController {

    @Autowired
    private ProviderService providerService;
    @Autowired
    private LanguageService languageService;

    @PostMapping("/saveprovider")
    public ResponseEntity<String> createProvider(
            @RequestParam String providerName, @RequestParam String bio, @RequestParam String email,
            @RequestParam String phone, @RequestParam String city, @RequestParam String country,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam String reviewText, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date reviewDate,
            @RequestParam String reviewCountry,
            @RequestParam int rating, @RequestParam(required = false) MultipartFile reviewImage,
            @RequestParam(required = false) MultipartFile documentFile) {
        System.out.println(reviewImage);
        providerService.addProviderWithReviewAndDocument(
                providerName, bio, email, phone, city, country, imageFile,
                reviewText, reviewDate, reviewCountry, rating, reviewImage,
                documentFile);
        return ResponseEntity.ok("Saved");
    }

    @GetMapping("/getProviderById")
    public ResponseEntity<Provider> getProviderById(@RequestParam long providerId) {
        Optional<Provider> provider = this.providerService.getProviderById(providerId);
        if (provider.isPresent()) {
            return new ResponseEntity<>(provider.get(), HttpStatus.OK);
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

    

    @PostMapping("/{providerId}/replace-documents")
    public ResponseEntity<String> replaceProviderDocument(@PathVariable Long providerId,
            @ModelAttribute DocumentList list) {
        System.out.println(list);
        String provider = providerService.replaceDocuments(providerId, list.getDocuments());
        return ResponseEntity.ok(provider);

    }

    @PutMapping("/{providerId}/update-profile")
    public ResponseEntity<String> updateProfile(@PathVariable long providerId, @ModelAttribute ProviderDto dto) {
        System.out.println(dto);
        String provider = providerService.updateProviderProfile(providerId, dto);
        return ResponseEntity.ok(provider);
    }

    @GetMapping("/get-all-providers")
    public ResponseEntity<ProviderResponse> getAllProviders(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "providerName", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String sortDir) {
        ProviderResponse allProvider = this.providerService.getAllProvider(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProvider, HttpStatus.OK);
    }

    @GetMapping("/{providerId}/get-language-by-providerId")
    public ResponseEntity<List<String>> getLanguageByProviderId(@PathVariable long providerId) {
        List<String> languages = this.providerService.getLanguageByProviderId(providerId);
        return ResponseEntity.ok(languages);
    }

    @GetMapping("/languages/{language}")
    public ResponseEntity<List<String>> searchLanguages(@PathVariable String language) {
        List<String> languages = this.languageService.searchLanguagesByName(language); // Search logic
        return ResponseEntity.ok(languages);
    }
}
