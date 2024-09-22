package com.service.provider.service;

import java.util.List;

import com.service.provider.dto.ReviewDto;

public interface ReviewService {

    String replaceProviderReviews(Long providerId, List<ReviewDto> list);

}
