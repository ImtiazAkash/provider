/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service.provider.service;

import com.service.provider.model.Documents;
import com.service.provider.repository.DocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class DocumentsServiceImpl implements DocumentsService{
    
    @Autowired
    private DocumentsRepository repository;
    

    @Override
    public Documents saveDocuments(Documents documents) {
        return this.repository.save(documents);
    }


    @Override
    public Documents findByProviderid(long providerid) {
        return this.repository.findByproviderid(providerid);
    }
    
}
