package com.service.provider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.provider.dto.ReviewDto;
import com.service.provider.model.ReviewList;
import com.service.provider.service.ReviewService;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/{providerId}/replace-reviews")
    public ResponseEntity<String> replaceProviderReviews(
            @PathVariable Long providerId,
            @ModelAttribute ReviewList list) {
        System.out.println("+++++++++++++++++++++++++++"+list);
        String provider = reviewService.replaceProviderReviews(providerId, list.getReviews());
        return ResponseEntity.ok(provider);

    }
}
