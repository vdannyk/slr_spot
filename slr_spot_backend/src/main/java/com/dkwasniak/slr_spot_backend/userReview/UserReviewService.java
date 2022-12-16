package com.dkwasniak.slr_spot_backend.userReview;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserReviewService {

    private final UserReviewRepository userReviewRepository;

    public Set<UserReview> getUserReviewByReviewId(Long reviewId) {
        return userReviewRepository.findByReview_Id(reviewId);
    }
}
