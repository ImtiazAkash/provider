/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.service.provider.service;

import com.service.provider.model.Review;

/**
 *
 * @author User
 */
public interface ReviewService {
    
    Review saveReview(Review review);

    Review findByProviderid(long providerid);
}
