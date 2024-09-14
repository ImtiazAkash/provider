package com.service.provider.service;

import com.service.provider.model.Profile;
import com.service.provider.repository.ProfileRepository;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository repository;
    
        

    @Override
    public Profile saveProfile(Profile profile) {
        return this.repository.save(profile);
    }

    @Override
    public Optional<Profile> findProfileById(long id) {
        return this.repository.findById(id);
    }

    @Override
    public Profile getProfileByProviderID(long providerID) {
        return this.repository.findByproviderid(providerID);
    }
    
    

    
}
