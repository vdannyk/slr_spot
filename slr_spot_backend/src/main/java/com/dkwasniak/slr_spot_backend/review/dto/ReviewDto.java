package com.dkwasniak.slr_spot_backend.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private String title;
    private String researchArea;
    private String description;
    private Boolean isPublic;
    private Integer screeningReviewers;
}
