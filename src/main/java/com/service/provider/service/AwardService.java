package com.service.provider.service;

import java.util.List;

import com.service.provider.dto.AwardDto;
import com.service.provider.model.Award;

public interface AwardService {
    public String replaceAwardsAndRecognition(long providerId, List<AwardDto> newAwards);

    public List<Award> getAwardByProviderId(long providerId);

    
}
