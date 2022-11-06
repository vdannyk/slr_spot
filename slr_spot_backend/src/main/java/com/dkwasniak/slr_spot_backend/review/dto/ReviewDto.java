package com.dkwasniak.slr_spot_backend.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private String name;
    private String researchArea;
    private String description;
    private Boolean isPublic;
    private Integer screeningReviewers;
    private Set<String> reviewers;
    private String protocol;
}
