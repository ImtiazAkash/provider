package com.service.provider.service;



import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;
import com.service.provider.dto.AwardDto;
import com.service.provider.dto.DocumentDto;
import com.service.provider.dto.ProviderDto;
import com.service.provider.dto.ProviderResponse;
import com.service.provider.dto.ReviewDto;
import com.service.provider.model.Provider;

public interface ProviderService {
    public void addProviderWithReviewAndDocument(
        String providerName, String bio, String email, String phone, String city, String country, MultipartFile imageFile,
        String reviewText, Date reviewDate, String reviewCountry, int rating, MultipartFile reviewImage,
        MultipartFile documentFile);

    public Optional<Provider> getProviderById(long providerId);

    public List<Provider> getAllProvider();

    public String addLanguagesToProvider(long providerId, Set<String>languageNames);

    



    public String replaceDocuments(long providerId, List<DocumentDto> newDocuments);

    public String updateProviderProfile(long providerId, ProviderDto profile);

    public ProviderResponse getAllProvider(int pageNumber, int pageSize, String sortBy, String sortDir);

    public List<String> getLanguageByProviderId(long providerId);
}
