package com.service.provider.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;
import lombok.Data;


@Data
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    private String review;
    private Date reviewDate;
    private String reviewCountry;
    private int rating;
    private String reviewImagePath;

    @ManyToOne
    @JoinColumn(name = "providerId")
    private Provider provider;


   

   


}
