package com.dkwasniak.slr_spot_backend.review;

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

    public Review saveProject(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByUser(String username) {
        return reviewRepository.findByUser(userService.getUser(username));
    }

    public Optional<Review> getReviewByTitle(String title) {
        return reviewRepository.findByTitle(title);
    }
}
