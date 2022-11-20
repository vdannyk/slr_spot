package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewMemberDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(EndpointConstants.API_PATH + "/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFacade reviewFacade;

    @GetMapping("/public")
    public ResponseEntity<ReviewsPageDto> getPublicReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(reviewFacade.getPublicReviews(page, size));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ReviewsPageDto> getReviewsByUser(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(reviewFacade.getReviewsByUser(id, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        // TODO Add validation if user can access this review
        return ResponseEntity.ok().body(reviewFacade.getReviewById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<Long> saveProject(@RequestBody ReviewDto reviewDto) {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok().body(reviewFacade.createReview(reviewDto, user));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<Set<ReviewMemberDto>> getMembers(@PathVariable Long id) {
        return ResponseEntity.ok().body(reviewFacade.getMembers(id));
    }
}
