package com.service.provider.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.provider.dto.AwardDto;
import com.service.provider.model.Award;
import com.service.provider.model.Provider;
import com.service.provider.repository.AwardRepository;
import com.service.provider.repository.ProviderRepository;

import jakarta.transaction.Transactional;

@Service
public class AwardServiceImpl implements AwardService{

    @Autowired
    private ProviderRepository providerRepository;


    @Autowired
    private AwardRepository awardRepository;
@Override
    @Transactional
    public String replaceAwardsAndRecognition(long providerId, List<AwardDto> newAwards) {
        try {
            System.out.println(newAwards);
            Provider provider = providerRepository.findById(providerId)
                    .orElseThrow(() -> new Exception("Provider not found with id " + providerId));

            // Delete all existing reviews
            Set<Long> awardIdsToDelete = provider.getAwards().stream().map(Award::getAwardId)
                    .collect(Collectors.toSet());

            if (!awardIdsToDelete.isEmpty()) {
                awardRepository.deleteAllById(awardIdsToDelete);
            }

            // Clear the existing reviews in memory
            provider.getAwards().clear();

            // Add new reviews
            for (AwardDto newAward : newAwards) {
                Award award = new Award();
                award.setAwardTitle(newAward.getAwardTitle());
                award.setAwardDescription(newAward.getAwardDescription());
                award.setAwardYear(newAward.getAwardYear());

                award.setProvider(provider);

                provider.getAwards().add(award);
            }

            // Save the updated provider
            providerRepository.save(provider);
            return "Awards updated successfully";
        } catch (Exception e) {

            e.printStackTrace();
            return "Updating awards failed";
        }
    }
@Override
public List<Award> getAwardByProviderId(long providerId) {
    return this.awardRepository.findAllByProviderProviderId(providerId);
}
}
