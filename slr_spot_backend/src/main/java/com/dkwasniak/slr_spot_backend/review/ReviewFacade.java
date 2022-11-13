package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewService reviewService;
    private final UserService userService;

    public long createReview(ReviewDto reviewDto, String username) {
        Review review = new Review(reviewDto.getName());
        long id = reviewService.saveReview(review);
        for (String email : reviewDto.getReviewers()) {
            User user = userService.getUserByEmail(email);
            userService.addReviewToUser(user, review);
        }
        return id;
    }

    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    public List<Review> getPublicReviews() {
        return reviewService.getPublicReviews();
    }

    public Review getReviewById(long id) {
        return reviewService.getReviewById(id);
    }

}
