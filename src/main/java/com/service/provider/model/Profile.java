package com.service.provider.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data

public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long profileId;
    private String providerName;
    private String bio;
    private String email;
    private String phone;
    private String city;
    private String country;
    private String imageFilePath;
    private long providerid;

    
    
    

}
