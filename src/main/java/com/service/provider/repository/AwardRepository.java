package com.service.provider.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.provider.model.Award;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long>{
    List<Award> findAllByProviderProviderId(long providerId);
}
