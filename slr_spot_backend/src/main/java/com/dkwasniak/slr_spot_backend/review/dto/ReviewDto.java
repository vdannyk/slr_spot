package com.dkwasniak.slr_spot_backend.review.dto;

import lombok.Data;

@Data
public class ReviewDto {

    private String title;
    private String researchArea;
    private String description;
    private Boolean isPublic;
    private Integer screeningReviewers;
}
