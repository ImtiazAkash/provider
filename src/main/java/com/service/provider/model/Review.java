package com.service.provider.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;
    @Column(length = 1000)
    private String review;
    private Date reviewDate;
    private String reviewCountry;
    private int rating;
    private String reviewImagePath;

    @ManyToOne
    @JoinColumn(name = "providerId")
    @JsonBackReference
    private Provider provider;
    @ManyToOne
    private User user;

}
