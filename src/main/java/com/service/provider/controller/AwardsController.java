package com.service.provider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.service.provider.dto.AwardDto;
import com.service.provider.model.Award;
import com.service.provider.service.AwardService;

@RestController
public class AwardsController {

    @Autowired
    private AwardService awardService;
 

    @PostMapping("/{providerId}/replace-awards")
    public ResponseEntity<String> replaceProviderAwards(@PathVariable Long providerId,
            @RequestBody List<AwardDto> list) {
        System.out.println(list);
        String provider = awardService.replaceAwardsAndRecognition(providerId, list);
        return ResponseEntity.ok(provider);

    }

   @GetMapping("/{providerId}/get-awards-by-providerId")
    public ResponseEntity<List<Award>> getAwardsByProviderId(@PathVariable long providerId) {
        List<Award> awards = this.awardService.getAwardByProviderId(providerId);
        return ResponseEntity.ok(awards);
    }
}
