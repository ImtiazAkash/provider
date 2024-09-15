package com.service.provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.service.provider.model.Award;

public interface AwardRepository extends JpaRepository<Award, Long>{

}
