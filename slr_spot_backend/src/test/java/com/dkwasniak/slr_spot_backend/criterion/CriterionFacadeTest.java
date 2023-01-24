package com.dkwasniak.slr_spot_backend.criterion;

import com.dkwasniak.slr_spot_backend.criterion.dto.CriterionDto;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CriterionFacadeTest {

    @InjectMocks
    private CriterionFacade criterionFacade;

    @Mock
    private CriterionService criterionService;
    @Mock
    private ReviewService reviewService;

    @Test
    public void getCriteriaByReviewId_shouldReturnReviewCriteria_whenExists() {
        var criteria = List.of(new Criterion(), new Criterion());

        when(criterionService.getCriteriaByReviewId(1L)).thenReturn(criteria);
        var criterionList = criterionFacade.getCriteriaByReviewId(1L);

        assertEquals(2, criterionList.size());
    }

    @Test
    public void addCriterion_shouldReturnCriterionId_whenAdded() {
        var criterion = new Criterion();
        var criterionDto = new CriterionDto(1L, "testName", CriterionType.INCLUSION.name());
        criterion.setId(1L);

        when(reviewService.getReviewById(criterionDto.getReviewId())).thenReturn(new Review());
        when(criterionService.saveCriterion(any())).thenReturn(criterion.getId());
        var saveCriterion = criterionFacade.addCriterion(criterionDto);

        assertEquals(1L, saveCriterion);
    }
}
