package com.service.provider.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.service.provider.dto.AwardDto;
import com.service.provider.dto.DocumentDto;
import com.service.provider.dto.ProviderDto;
import com.service.provider.dto.ProviderResponse;
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



    private ModelMapper modelMapper = ModelMapperST.getInstance();

    @Value("${project.image}")
    private String imageDir;

    @Override
    public void addProviderWithReviewAndDocument(String providerName, String bio, String email, String phone,
            String city, String country, MultipartFile imageFile, String reviewText, Date reviewDate,
            String reviewCountry, int rating, MultipartFile reviewImage, MultipartFile documentFile) {

        File uploadDir = new File(imageDir);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String providerImagePath = "";
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            File destinationFile = new File(imageDir + fileName);
            try {
                imageFile.transferTo(destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            providerImagePath = fileName;
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
            String fileName = UUID.randomUUID().toString() + "_" + reviewImage.getOriginalFilename();
            File destinationFile = new File(imageDir + fileName);
            try {
                reviewImage.transferTo(destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            reviewImagePathString = fileName;
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
            String fileName = UUID.randomUUID().toString() + "_" + documentFile.getOriginalFilename();
            File destinationFile = new File(imageDir + fileName);
            try {
                documentFile.transferTo(destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            documentFilePathString = fileName;
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

                File uploadDir = new File(imageDir);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String reviewImagePathString = "";
                if (newReview.getReviewImage() != null && !newReview.getReviewImage().isEmpty()) {
                    String fileName = UUID.randomUUID().toString() + "_"
                            + newReview.getReviewImage().getOriginalFilename();
                    File destinationFile = new File(imageDir + fileName);
                    try {
                        newReview.getReviewImage().transferTo(destinationFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    reviewImagePathString = fileName;
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

                File uploadDir = new File(imageDir);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String documentFilePathString = "";
                if (newDocument.getDocumentFile() != null && !newDocument.getDocumentFile().isEmpty()) {
                    String fileName = UUID.randomUUID().toString() + "_"
                            + newDocument.getDocumentFile().getOriginalFilename();
                    File destinationFile = new File(imageDir + fileName);
                    try {
                        newDocument.getDocumentFile().transferTo(destinationFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    documentFilePathString = fileName;
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
    @Transactional
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

            File uploadDir = new File(imageDir);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String providerImagePath = "";
            if (profile.getImageFile() != null && !profile.getImageFile().isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + profile.getImageFile().getOriginalFilename();
                String destinationFile = imageDir + File.separator + fileName;
                System.out.println("++++++++++++++++++++++++++++++++++"+destinationFile);
                try {
                    Files.copy(profile.getImageFile().getInputStream(), Paths.get(destinationFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                providerImagePath = fileName;
            }
            provider.setImageFilePath(providerImagePath);

            providerRepository.save(provider);
            return "Profile updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Profile updating failed";

        }
    }

    @Override
    public ProviderResponse getAllProvider(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        System.out.println(pageNumber - 1 + " " + pageSize);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Provider> pageProvider = this.providerRepository.findAll(pageable);
        System.out.println(pageProvider.getContent());

        List<Provider> providers = pageProvider.getContent();

        ProviderResponse providerResponse = new ProviderResponse();

        providerResponse.setProviders(providers);
        providerResponse.setPageNumber(pageProvider.getNumber());
        providerResponse.setPageSize(pageProvider.getSize());
        providerResponse.setTotalItems(pageProvider.getTotalElements());
        providerResponse.setTotalPages(pageProvider.getTotalPages());
        providerResponse.setLastPage(pageProvider.isLast());
        System.out.println(pageProvider.getTotalPages());
        return providerResponse;

    }

    public ProviderDto convertToDto(Provider provider) {

        return modelMapper.map(provider, ProviderDto.class);
    }

}
