package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFacade reviewFacade;
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<Review>> getAllProjects() {
        return ResponseEntity.ok().body(reviewService.getReviews());
    }

    @GetMapping("/yours")
    public ResponseEntity<List<Review>> getUserProjects() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok().body(reviewService.getReviewsByUser(username));
    }

    @PostMapping("/save")
    public ResponseEntity<List<Review>> saveProject(@RequestBody ReviewDto reviewDto) {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        reviewFacade.createProject(reviewDto, user);
        return ResponseEntity.ok().build();
    }
}
