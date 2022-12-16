package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewMembersDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewWithOwnerDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@Controller
@RequestMapping(EndpointConstants.API_PATH + "/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFacade reviewFacade;

    @GetMapping
    public ResponseEntity<ReviewsPageDto> getReviewsByUser(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(reviewFacade.getReviewsByUserId(userId, page, size));
    }

    @GetMapping("/public")
    public ResponseEntity<ReviewsPageDto> getPublicReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(reviewFacade.getPublicReviews(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewWithOwnerDto> getReviewById(@PathVariable Long id) {
        // TODO Add validation if user can access this review
        return ResponseEntity.ok().body(reviewFacade.getReviewById(id));
    }

    @PostMapping
    public ResponseEntity<Long> saveProject(@RequestBody ReviewDto reviewDto) {
        long id = reviewFacade.addReview(reviewDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path(EndpointConstants.API_PATH + "/reviews").buildAndExpand(id).toUriString());
        return ResponseEntity.created(uri).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        reviewFacade.updateReview(id, reviewDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/members/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long id, @PathVariable Long memberId) {
        reviewFacade.removeMember(id, memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/members/search")
    public ResponseEntity<Set<String>> getUsersAvailableToAdd(@PathVariable Long id) {
        return ResponseEntity.ok().body(reviewFacade.getUsersAvailableToAdd(id));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Void> addMembers(@PathVariable Long id, @RequestBody ReviewMembersDto reviewMembersDto) {
        reviewFacade.addMembers(id, reviewMembersDto);
        return ResponseEntity.ok().build();
    }

}
