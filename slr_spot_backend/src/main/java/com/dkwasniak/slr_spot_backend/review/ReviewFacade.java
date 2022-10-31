package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewService reviewService;
    private final UserService userService;

    public long createProject(ReviewDto reviewDto, String username) {
        User user = userService.getUser(username);
        Review review = reviewService.saveProject(new Review(reviewDto.getTitle(), user));
        return review.getId();
    }
}
