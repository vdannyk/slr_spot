package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.exception.ReviewNotFoundException;
import com.dkwasniak.slr_spot_backend.role.Role;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public Long saveReview(Review review) {
        return reviewRepository.save(review).getId();
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getPublicReviews() {
        return reviewRepository.findAllPublic();
    }

    public List<Review> getReviewsByUser(String username) {
        return reviewRepository.findByUsers_Email(username);
    }

    public Review getReviewByTitle(String title) {
        return reviewRepository.findByTitle(title)
                .orElseThrow(() -> new ReviewNotFoundException("Review with given title not found"));
    }

    public Review getReviewById(long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with given id not found"));
    }

}
