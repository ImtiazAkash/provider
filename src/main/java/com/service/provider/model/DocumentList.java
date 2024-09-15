package com.service.provider.model;

import java.util.List;
import com.service.provider.dto.DocumentDto;
import lombok.Data;

@Data
public class DocumentList {
    private List<DocumentDto> documents;
}
