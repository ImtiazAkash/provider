package com.service.provider.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import com.service.provider.model.Provider;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ReviewDto {
private long reviewId;

    private String review;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reviewDate;
    private String reviewCountry;
    private int rating;
    private MultipartFile reviewImage;

    
    private Provider provider;
}
