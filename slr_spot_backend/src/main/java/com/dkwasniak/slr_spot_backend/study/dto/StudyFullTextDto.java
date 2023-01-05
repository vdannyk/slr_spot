package com.dkwasniak.slr_spot_backend.study.dto;

import com.dkwasniak.slr_spot_backend.study.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyFullTextDto {

    private Study study;
    private byte[] content;
}
