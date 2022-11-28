package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.criterion.Criterion;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWord;
import com.dkwasniak.slr_spot_backend.review.dto.AddMembersDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewWithOwnerDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewMemberDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.tag.Tag;
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
    public ResponseEntity<ReviewWithOwnerDto> getReviewById(@PathVariable Long id) {
        // TODO Add validation if user can access this review
        return ResponseEntity.ok().body(reviewFacade.getReviewById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<Long> saveProject(@RequestBody ReviewDto reviewDto) {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok().body(reviewFacade.createReview(reviewDto, user));
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<Void> updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        reviewFacade.updateReview(id, reviewDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<Set<ReviewMemberDto>> getMembers(@PathVariable Long id) {
        return ResponseEntity.ok().body(reviewFacade.getMembers(id));
    }

    @PostMapping("/{id}/members/{userId}/remove")
    public ResponseEntity<Void> removeMember(@PathVariable Long id, @PathVariable Long userId) {
        reviewFacade.removeMember(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/members/search")
    public ResponseEntity<Set<String>> getUsersAvailableToAdd(@PathVariable Long id) {
        return ResponseEntity.ok().body(reviewFacade.getUsersAvailableToAdd(id));
    }

    @PostMapping("/{id}/members/add")
    public ResponseEntity<Void> addMembers(@PathVariable Long id, @RequestBody AddMembersDto membersToAdd) {
        reviewFacade.addMembers(id, membersToAdd);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<Set<Tag>> getTags(@PathVariable Long id) {
        return ResponseEntity.ok().body(reviewFacade.getTags(id));
    }

    @GetMapping("/{reviewId}/tags/add")
    public ResponseEntity<Void> addTag(@PathVariable Long reviewId, @RequestParam String name) {
        reviewFacade.addTag(reviewId, name);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/tags/{tagId}/remove")
    public ResponseEntity<Void> removeTag(@PathVariable Long id, @PathVariable Long tagId) {
        reviewFacade.removeTag(id, tagId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/criteria")
    public ResponseEntity<Set<Criterion>> getCriteria(@PathVariable Long id) {
        return ResponseEntity.ok().body(reviewFacade.getCriteria(id));
    }

    @GetMapping("/{reviewId}/criteria/add")
    public ResponseEntity<Void> addTag(@PathVariable Long reviewId,
                                       @RequestParam String name,
                                       @RequestParam String type) {
        reviewFacade.addCriterion(reviewId, name, type);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reviewId}/criteria/{criterionId}/{criterionTypeId}/remove")
    public ResponseEntity<Void> removeTag(@PathVariable Long reviewId,
                                          @PathVariable Long criterionId,
                                          @PathVariable Long criterionTypeId) {
        reviewFacade.removeCriterion(reviewId, criterionId, criterionTypeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/keywords")
    public ResponseEntity<Set<KeyWord>> getKeywords(@PathVariable Long id) {
        return ResponseEntity.ok().body(reviewFacade.getKeywords(id));
    }

    @GetMapping("/{reviewId}/keywords/add")
    public ResponseEntity<Void> addKeyword(@PathVariable Long reviewId,
                                       @RequestParam String name,
                                       @RequestParam String type) {
        reviewFacade.addKeyword(reviewId, name, type);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reviewId}/keywords/{keywordId}/{keywordTypeId}/remove")
    public ResponseEntity<Void> removeKeyword(@PathVariable Long reviewId,
                                          @PathVariable Long keywordId,
                                          @PathVariable Long keywordTypeId) {
        reviewFacade.removeKeyword(reviewId, keywordId, keywordTypeId);
        return ResponseEntity.ok().build();
    }

}
