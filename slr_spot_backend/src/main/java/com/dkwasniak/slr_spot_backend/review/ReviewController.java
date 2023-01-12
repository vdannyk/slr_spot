package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewMembersDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewWithOwnerDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleEnum;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Value("${jwt.secret}")
    private String jwtSecret;

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

    @PostAuthorize("hasViewAccess(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewWithOwnerDto> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok().body(reviewFacade.getReviewById(id));
    }

    @GetMapping("/{id}/is-public")
    public ResponseEntity<Boolean> getIsPublic(@PathVariable Long id) {
        return ResponseEntity.ok().body(reviewFacade.getIsPublic(id));
    }

    @PostMapping
    public ResponseEntity<Long> saveProject(@RequestBody ReviewDto reviewDto) {
        long id = reviewFacade.addReview(reviewDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path(EndpointConstants.API_PATH + "/reviews").buildAndExpand(id).toUriString());
        return ResponseEntity.created(uri).body(id);
    }

    @PostAuthorize("hasFullAccess(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        reviewFacade.updateReview(id, reviewDto);
        return ResponseEntity.ok().build();
    }

    @PostAuthorize("hasFullAccess(#id)")
    @PostMapping("/{id}/members/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long id, @PathVariable Long memberId) {
        reviewFacade.removeMember(id, memberId);
        return ResponseEntity.ok().build();
    }

    @PostAuthorize("hasFullAccess(#id)")
    @GetMapping("/{id}/members/search")
    public ResponseEntity<Set<String>> getUsersAvailableToAdd(@PathVariable Long id) {
        return ResponseEntity.ok().body(reviewFacade.getUsersAvailableToAdd(id));
    }

    @PostAuthorize("hasFullAccess(#id)")
    @PostMapping("/{id}/members")
    public ResponseEntity<Void> addMembers(@PathVariable Long id, @RequestBody ReviewMembersDto reviewMembersDto) {
        reviewFacade.addMembers(id, reviewMembersDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/members/{userId}/role")
    public ResponseEntity<String> getMemberRole(@PathVariable Long id,
                                                @PathVariable Long userId) {
        return ResponseEntity.ok().body(reviewFacade.getMemberRole(id, userId));
    }

    @PostAuthorize("hasScreeningAccess(#id)")
    @GetMapping("/{id}/report")
    public ResponseEntity<Resource> generateReviewReport(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename=study.pdf");
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(reviewFacade.generateReviewReport(id));
    }

}
