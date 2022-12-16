package com.dkwasniak.slr_spot_backend.tag;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class TagFacade {

    private final TagService tagService;
    private final ReviewService reviewService;

    public Set<Tag> getTagsByReviewId(Long reviewId) {
        return tagService.getTagsByReviewId(reviewId);
    }

    public Long addTag(Long reviewId, String name) {
        Review review = reviewService.getReviewById(reviewId);
        Tag tag = new Tag(name);
        tag.setReview(review);
        return tagService.saveTag(tag);
    }

    public void removeTag(Long tagId) {
        tagService.removeTag(tagId);
    }
}
