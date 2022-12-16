package com.dkwasniak.slr_spot_backend.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class ReviewsPageDto {

    private Set<ReviewWithOwnerDto> reviews;
    private int currentPage;
    private long totalReviewsNum;
    private int totalPagesNum;
}
