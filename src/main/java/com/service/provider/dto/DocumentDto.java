package com.service.provider.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class DocumentDto {
    private long documentId;
    private MultipartFile documentFile;
    private ProviderDto provider;
}
