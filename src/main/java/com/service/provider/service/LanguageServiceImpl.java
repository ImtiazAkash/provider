package com.service.provider.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.provider.model.Language;
import com.service.provider.repository.LanguageRepository;

@Service
public class LanguageServiceImpl implements LanguageService{

    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public List<String> searchLanguagesByName(String query) {
        List<Language> languageList = this.languageRepository.findByLanguageContaining(query);

        List<String> languages = languageList.stream().map(Language::getLanguage).collect(Collectors.toList());
        return languages;
    }

}
