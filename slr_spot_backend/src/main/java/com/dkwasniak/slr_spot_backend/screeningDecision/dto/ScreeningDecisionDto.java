package com.dkwasniak.slr_spot_backend.screeningDecision.dto;

import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ScreeningDecisionDto {

    private Long reviewId;
    private Long userId;
    private Decision decision;
}
