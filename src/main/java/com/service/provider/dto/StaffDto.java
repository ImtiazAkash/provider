package com.service.provider.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class StaffDto {
private long id;

    private String name;
    private String position;
    private MultipartFile imageFile; // Assuming this will store the path or URL to the image

    
    private ProviderDto provider;
}
