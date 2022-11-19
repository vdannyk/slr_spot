package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.review.exception.ReviewNotFoundException;
import com.dkwasniak.slr_spot_backend.role.Role;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Page<Review> getPublicReviews(int page, int size) {
        PageRequest pageRq = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "title"));
        return reviewRepository.findAllPublic(pageRq);
    }

    public Page<Review> getReviewsByUser(long userId, int page, int size) {
        PageRequest pageRq = PageRequest.of(page, size, Sort.by("title"));
        return reviewRepository.findByUsers_User_Id(userId, pageRq);
    }

    public ReviewsPageDto toReviewsPageDto(Page<Review> reviewsPage) {
        return ReviewsPageDto.builder()
                .reviews(new HashSet<>(reviewsPage.getContent()))
                .currentPage(reviewsPage.getNumber())
                .totalReviewsNum(reviewsPage.getTotalElements())
                .totalPagesNum(reviewsPage.getTotalPages())
                .build();

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
