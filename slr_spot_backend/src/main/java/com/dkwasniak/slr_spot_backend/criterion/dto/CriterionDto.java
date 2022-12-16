package com.dkwasniak.slr_spot_backend.criterion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CriterionDto {

    private Long reviewId;
    private String name;
    private String type;
}
