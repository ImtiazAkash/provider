package com.service.provider.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.service.provider.model.Language;

public interface LanguageRepository extends JpaRepository<Language, Long>{

    Language findByLanguage(String language);

    @Query("SELECT l FROM Language l WHERE LOWER(l.language) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Language> findByLanguageContaining(@Param("query") String query);
}
