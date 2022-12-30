package com.dkwasniak.slr_spot_backend.deduplication.dto;

import com.dkwasniak.slr_spot_backend.study.Study;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DeduplicationDto {

    private List<Study> correctStudies;
    private int numOfRemovedDuplicates;
}
