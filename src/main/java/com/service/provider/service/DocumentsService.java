/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.service.provider.service;

import com.service.provider.model.Documents;

/**
 *
 * @author User
 */
public interface DocumentsService {
    Documents saveDocuments(Documents documents);

    Documents findByProviderid(long providerid);
}
