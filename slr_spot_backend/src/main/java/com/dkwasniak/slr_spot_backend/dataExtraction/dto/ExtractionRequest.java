package com.dkwasniak.slr_spot_backend.dataExtraction.dto;

import com.dkwasniak.slr_spot_backend.dataExtraction.ExtractionField;
import com.dkwasniak.slr_spot_backend.study.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExtractionRequest {

    private List<ExtractionField> fields;
    private List<Study> studies;
}
