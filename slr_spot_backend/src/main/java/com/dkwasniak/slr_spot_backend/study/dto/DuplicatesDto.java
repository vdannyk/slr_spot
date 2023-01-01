package com.dkwasniak.slr_spot_backend.study.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DuplicatesDto {

    private List<Long> studiesId;
}
