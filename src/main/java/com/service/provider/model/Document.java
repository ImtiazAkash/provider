package com.service.provider.model;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Document{

      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long documentId;

    private String documentFilePath;

    @ManyToOne
    @JoinColumn(name = "providerId")
    private Provider provider;

}
