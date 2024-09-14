/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service.provider.model;

import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */

@Data
public class ProviderDto {
    private long profileId;
    private String providerName;
    private String bio;
    private String email;
    private String phone;
    private String city;
    private String country;
    private MultipartFile imageFile;
    private String profileImageURL;
    
    private long reviewId;
    private String review;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reviewDate;
    private String reviewCountry;
    private int rating;
    private MultipartFile reviewImage;
    private String reviewImageURL;
    
    private long documentId;
    private MultipartFile documentFile;
    private String documentFileURL;
}
