package com.service.provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.provider.model.Language;

public interface LanguageRepository extends JpaRepository<Language, Long>{

    Language findByLanguage(String language);
}
