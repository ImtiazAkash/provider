package com.service.provider.dto;

import java.util.List;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;


@Data
public class ProviderDto {
    private long providerId;

    private String providerName;
    private String bio;
    private String email;
    private String phone;
    private String city;
    private String country;
    private MultipartFile imageFile;


    private List<ReviewDto> reviews;


    private List<DocumentDto> documents;


    private List<StaffDto> staff;


    private List<AwardDto> awards;


    private Set<LanguageDto> languages;
}
