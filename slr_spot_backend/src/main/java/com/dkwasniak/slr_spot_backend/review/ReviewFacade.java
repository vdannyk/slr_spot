package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewMemberDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewService reviewService;
    private final UserService userService;

    public long createReview(ReviewDto reviewDto, String username) {
        Review review = new Review(reviewDto.getName(), reviewDto.getResearchArea(), reviewDto.getDescription(),
                reviewDto.getIsPublic(), reviewDto.getScreeningReviewers(), username);
        long id = reviewService.saveReview(review);
        User owner = userService.getUserByEmail(username);
        userService.addReviewToUser(owner, review);
        for (String email : reviewDto.getReviewers()) {
            User user = userService.getUserByEmail(email);
            userService.addReviewToUser(user, review);
        }
        return id;
    }

    public ReviewsPageDto getPublicReviews(int page, int size) {
        return reviewService.toReviewsPageDto(
                reviewService.getPublicReviews(page, size)
        );
    }

    public Review getReviewById(long id) {
        return reviewService.getReviewById(id);
    }

    public Set<ReviewMemberDto> getMembers(long id) {
        return reviewService.getMembers(id);
    }

    public ReviewsPageDto getReviewsByUser(long userId, int page, int size) {
        return reviewService.toReviewsPageDto(
                reviewService.getReviewsByUser(userId, page, size)
        );
    }
}
