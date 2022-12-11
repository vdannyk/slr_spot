package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointConstants.API_PATH + "/studies")
public class StudyController {

    private final StudyFacade studyFacade;

    @GetMapping
    public ResponseEntity<List<Study>> getStudiesByReviewId(@RequestParam("reviewId") Long reviewId) {
        return ResponseEntity.ok(studyFacade.getStudiesByReviewId(reviewId));
    }
}
