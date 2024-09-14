package com.service.provider.service;

import com.service.provider.model.Profile;
import java.util.Optional;

public interface ProfileService {

    
    Profile saveProfile(Profile profile);
    
    Optional<Profile> findProfileById(long id);

    Profile getProfileByProviderID(long providerid);
    
    

   
}
