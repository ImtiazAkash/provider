package com.service.provider.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Documents{

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long documentId;
    private String documentFilePath;
    private long providerid;

}
