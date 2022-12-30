package com.dkwasniak.slr_spot_backend.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IdentificationDto {

    private String title;
    private String authors;
    private Integer publicationYear;

}
