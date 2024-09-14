/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.service.provider.repository;

import com.service.provider.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author User
 */
public interface DocumentsRepository extends JpaRepository<Document, Long>{
    
    
}
