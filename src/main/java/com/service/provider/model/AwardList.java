package com.service.provider.model;

import java.util.List;
import com.service.provider.dto.AwardDto;
import lombok.Data;

@Data
public class AwardList {
    private List<AwardDto> awards;
}
