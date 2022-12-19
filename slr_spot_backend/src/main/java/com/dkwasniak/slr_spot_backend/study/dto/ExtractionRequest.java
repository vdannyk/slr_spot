package com.dkwasniak.slr_spot_backend.study.dto;

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

    private List<String> fields;
    private List<Study> studies;
}
