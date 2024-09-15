package com.service.provider.dto;

import lombok.Data;

@Data
public class AwardDto {
    private long awardId;
    private String awardTitle;
    private String awardDescription;
    private int awardYear;
    private ProviderDto provider;
}
