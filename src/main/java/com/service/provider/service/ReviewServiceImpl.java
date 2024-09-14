/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service.provider.service;

import com.service.provider.model.Review;
import com.service.provider.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service 
public class ReviewServiceImpl implements ReviewService{
    
    @Autowired
    private ReviewRepository repository;
    

    @Override
    public Review saveReview(Review review) {
        return this.repository.save(review);
    }


    @Override
    public Review findByProviderid(long providerid) {
        return this.repository.findByproviderid(providerid);
    }
    
}
