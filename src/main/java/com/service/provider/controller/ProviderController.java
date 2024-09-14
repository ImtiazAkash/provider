/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service.provider.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
