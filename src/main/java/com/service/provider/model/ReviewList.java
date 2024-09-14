package com.service.provider.model;

import java.util.List;

import com.service.provider.dto.ReviewDto;

import lombok.Data;

@Data
public class ReviewList {
private List<ReviewDto> reviews;

}
