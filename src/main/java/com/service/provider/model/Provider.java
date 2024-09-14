package com.service.provider.model;



import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;




@Entity
@Data

public class Provider {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long providerId;

    private String providerName;
    private String bio;
    private String email;
    private String phone;
    private String city;
    private String country;
    private String imageFilePath;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Document> documents;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Staff> staff;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Award> awards;

    @ManyToMany
    @JoinTable(
        name = "provider_language", // Name of the join table
        joinColumns = @JoinColumn(name = "providerId"), // Foreign key for Profile
        inverseJoinColumns = @JoinColumn(name = "languageId") // Foreign key for Language
    )
    @JsonManagedReference
    private Set<Language> languages;
    
    

}
