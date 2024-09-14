package com.service.provider.model;

import org.springframework.context.annotation.Profile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long awardId;

    private String awardTitle;
    private String awardDescription;
    private int awardYear;

    @ManyToOne
    @JoinColumn(name = "providerId")
    private Provider provider; 
}
