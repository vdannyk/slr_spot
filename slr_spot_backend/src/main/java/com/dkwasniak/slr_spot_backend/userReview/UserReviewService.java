package com.dkwasniak.slr_spot_backend.userReview;

import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleEnum;
import com.dkwasniak.slr_spot_backend.userReview.exception.UserReviewNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserReviewService {

    private final UserReviewRepository userReviewRepository;

    public void changeUserReviewRole(UserReview userReview, ReviewRole newRole) {
        ReviewRole reviewRole = userReview.getRole();
        if (ReviewRoleEnum.OWNER.name().equals(reviewRole.getName())) {
            throw new IllegalStateException();
        }
        userReview.setRole(newRole);
    }

    public List<UserReview> getUserReviewByReviewId(Long reviewId) {
        return userReviewRepository.findByReview_IdOrderByUser_LastName(reviewId);
    }

    public UserReview getUserReviewByReviewIdAndUserId(Long reviewId, Long userId) {
        return userReviewRepository.findByReview_IdAndUser_Id(reviewId, userId)
                .orElseThrow(() -> new UserReviewNotFoundException("User review not found"));
    }
}
