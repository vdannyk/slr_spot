package com.dkwasniak.slr_spot_backend.keyWord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KeyWordDto {

    private Long reviewId;
    private Long userId;
    private String name;
    private String type;
}
