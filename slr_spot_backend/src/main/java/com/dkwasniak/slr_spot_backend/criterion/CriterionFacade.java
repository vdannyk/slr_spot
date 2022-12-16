package com.dkwasniak.slr_spot_backend.criterion;

import com.dkwasniak.slr_spot_backend.criterion.dto.CriterionDto;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CriterionFacade {

    private final CriterionService criterionService;
    private final ReviewService reviewService;

    public List<Criterion> getCriteriaByReviewId(Long reviewId) {
        return criterionService.getCriteriaByReviewId(reviewId);
    }

    public Long addCriterion(CriterionDto criterionDto) {
        Review review = reviewService.getReviewById(criterionDto.getReviewId());
        Criterion criterion = new Criterion(criterionDto.getName(), CriterionType.valueOf(criterionDto.getType()));
        criterion.setReview(review);
        return criterionService.saveCriterion(criterion);
    }

    public void removeCriterion(Long criterionId) {
        criterionService.removeCriterion(criterionId);
    }
}
