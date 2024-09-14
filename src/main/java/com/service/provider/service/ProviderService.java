package com.service.provider.service;



import org.springframework.web.multipart.MultipartFile;

import com.service.provider.dto.ReviewDto;
import com.service.provider.model.Provider;
import com.service.provider.model.Review;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProviderService {
    public void addProviderWithReviewAndDocument(
        String providerName, String bio, String email, String phone, String city, String country, MultipartFile imageFile,
        String reviewText, Date reviewDate, String reviewCountry, int rating, MultipartFile reviewImage,
        MultipartFile documentFile);

    public Optional<Provider> getProviderById(long providerId);

    public List<Provider> getAllProvider();

    public String addLanguagesToProvider(long providerId, Set<String>languageNames);

    public String replaceProviderReviews(Long providerId, List<ReviewDto> newReviews);
}
