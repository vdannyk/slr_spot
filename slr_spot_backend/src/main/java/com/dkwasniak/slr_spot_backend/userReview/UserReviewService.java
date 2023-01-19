package com.dkwasniak.slr_spot_backend.userReview;

import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleEnum;
import com.dkwasniak.slr_spot_backend.userReview.exception.UserReviewNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


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

    public Page<UserReview> getUserReviewByReviewId(Long reviewId, int page, int size) {
        PageRequest pageRq = PageRequest.of(page, size);
        return userReviewRepository.findByReview_IdOrderByUser_LastName(reviewId, pageRq);
    }

    public UserReview getUserReviewByReviewIdAndUserId(Long reviewId, Long userId) {
        return userReviewRepository.findByReview_IdAndUser_Id(reviewId, userId)
                .orElseThrow(() -> new UserReviewNotFoundException("User review not found"));
    }
}
