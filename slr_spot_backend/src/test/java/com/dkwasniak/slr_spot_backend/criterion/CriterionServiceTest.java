package com.dkwasniak.slr_spot_backend.criterion;

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
public class CriterionServiceTest {

    @InjectMocks
    private CriterionService criterionService;

    @Mock
    private CriterionRepository criterionRepository;

    @Test
    public void getCriteriaByReviewId_shouldReturnReviewCriteria_whenExists() {
        var criteria = List.of(new Criterion(), new Criterion());

        when(criterionRepository.findByReview_Id(1L)).thenReturn(criteria);
        var criterionList = criterionService.getCriteriaByReviewId(1L);

        assertEquals(2, criterionList.size());
    }

    @Test
    public void getTagsByReviewId_shouldReturnCriterionId_whenAdded() {
        var criterion = new Criterion();
        criterion.setId(1L);

        when(criterionRepository.save(any())).thenReturn(criterion);
        var saveCriterion = criterionService.saveCriterion(criterion);

        assertEquals(1L, saveCriterion);
    }
}
