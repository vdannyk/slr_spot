package com.dkwasniak.slr_spot_backend.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewDto {

    private Long userId;
    private String name;
    private String researchArea;
    private String description;
    private Boolean isPublic;
    private Integer screeningReviewers;
    private Set<String> reviewers;
    private List<String> researchQuestions;
}
