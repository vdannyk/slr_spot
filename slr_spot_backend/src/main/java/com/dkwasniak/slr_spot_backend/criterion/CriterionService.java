package com.dkwasniak.slr_spot_backend.criterion;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CriterionService {

    private final CriterionRepository criterionRepository;

    public List<Criterion> getCriteriaByReviewId(Long reviewId) {
        return criterionRepository.findByReview_Id(reviewId);
    }

    public Long saveCriterion(Criterion criterion) {
        return criterionRepository.save(criterion).getId();
    }

    public void removeCriterion(Long criterionId) {
        criterionRepository.deleteById(criterionId);
    }
}
