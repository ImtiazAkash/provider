package com.service.provider.dto;

import java.util.List;

import com.service.provider.model.Provider;

import lombok.Data;

@Data
public class ProviderResponse {
    private List<Provider> providers;
    private int pageNumber;
    private int pageSize;
    private long totalItems;
    private int totalPages;
    private boolean lastPage;
}
