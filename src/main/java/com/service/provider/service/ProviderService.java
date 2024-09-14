package com.service.provider.service;



import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public interface ProviderService {
    public void addProviderWithReviewAndDocument(
        String providerName, String bio, String email, String phone, String city, String country, MultipartFile imageFile,
        String reviewText, Date reviewDate, String reviewCountry, int rating, MultipartFile reviewImage,
        MultipartFile documentFile);
}
