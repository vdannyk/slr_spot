package com.dkwasniak.slr_spot_backend.criterion;

import com.dkwasniak.slr_spot_backend.criterion.dto.CriterionDto;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(EndpointConstants.API_PATH + "/criteria")
@RequiredArgsConstructor
public class CriterionController {

    private final CriterionFacade criterionFacade;

    @GetMapping
    public ResponseEntity<List<Criterion>> getCriteria(@RequestParam Long reviewId) {
        return ResponseEntity.ok().body(criterionFacade.getCriteriaByReviewId(reviewId));
    }

    @PostMapping
    public ResponseEntity<Long> addCriterion(@RequestBody CriterionDto criterionDto) {
        long id = criterionFacade.addCriterion(criterionDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path(EndpointConstants.API_PATH + "/criteria").buildAndExpand(id).toUriString());
        return ResponseEntity.created(uri).body(id);
    }

    @DeleteMapping("/{criterionId}")
    public ResponseEntity<Void> deleteCriterion(@PathVariable Long criterionId) {
        criterionFacade.removeCriterion(criterionId);
        return ResponseEntity.ok().build();
    }
}
