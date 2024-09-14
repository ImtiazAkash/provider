package com.service.provider.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.service.provider.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>{

    // @Query(value = "select * from profile where providerid=?1", nativeQuery = true)
    Profile findByproviderid(long providerID);
}
