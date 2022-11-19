package com.dkwasniak.slr_spot_backend.review.dto;

import com.dkwasniak.slr_spot_backend.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class ReviewsPageDto {

    private Set<Review> reviews;
    private int currentPage;
    private long totalReviewsNum;
    private int totalPagesNum;
}
