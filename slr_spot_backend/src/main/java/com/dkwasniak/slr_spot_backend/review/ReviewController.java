package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(EndpointConstants.API_PATH + "/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFacade reviewFacade;

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok().body(reviewFacade.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        // TODO Add validation if user can access this review
        return ResponseEntity.ok().body(reviewFacade.getReviewById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<List<Review>> saveProject(@RequestBody ReviewDto reviewDto) {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        reviewFacade.createReview(reviewDto, user);
        return ResponseEntity.ok().build();
    }
}
