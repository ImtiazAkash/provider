package com.service.provider.model;

import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long awardId;

    private String awardTitle;
    private String awardDescription;
    private int awardYear;

    @ManyToOne
    @JoinColumn(name = "providerId")
    @JsonBackReference
    private Provider provider; 
}
