package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewMemberDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.review.exception.ReviewNotFoundException;
import com.dkwasniak.slr_spot_backend.role.ReviewRole;
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
                .reviews(reviewsPage.getContent().stream().map(this::boundWithOwner).collect(Collectors.toSet()))
                .currentPage(reviewsPage.getNumber())
                .totalReviewsNum(reviewsPage.getTotalElements())
                .totalPagesNum(reviewsPage.getTotalPages())
                .build();
    }

    protected ReviewDto boundWithOwner(Review review) {
        User owner = review.getUsers().stream()
                .filter(user -> ReviewRole.OWNER.equals(user.getRole())).findFirst().get().getUser();
        return ReviewDto.of(owner.getFirstName(), owner.getLastName(), review);
    }

    public Set<ReviewMemberDto> getMembers(long id) {
        Set<UserReview> userReviews = getReviewById(id).getUsers();
        return userReviews.stream()
                .map((userReview) -> toReviewMemberDto(userReview.getUser(), userReview.getRole()))
                .collect(Collectors.toSet());
    }

    public ReviewMemberDto toReviewMemberDto(User user, ReviewRole role) {
        return ReviewMemberDto.builder()
                .memberId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(role)
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

    public void removeMember(long reviewId, long userId) {
        User user = userService.getUserById(userId);
        Review review = getReviewById(reviewId);
        user.removeReview(review);
    }
}
