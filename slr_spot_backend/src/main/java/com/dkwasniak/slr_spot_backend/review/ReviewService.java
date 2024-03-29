package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.researchQuestion.ResearchQuestion;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewWithOwnerDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.review.exception.ReviewNotFoundException;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleEnum;
import com.dkwasniak.slr_spot_backend.user.User;
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

    public Long saveReview(Review review) {
        return reviewRepository.save(review).getId();
    }

    public void deleteById(long id) {
        reviewRepository.deleteById(id);
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

    protected ReviewWithOwnerDto boundWithOwner(Review review) {
        User owner = review.getUsers().stream()
                .filter(user -> ReviewRoleEnum.OWNER.name().equals(user.getRole().getName())).findFirst().get().getUser();
        return ReviewWithOwnerDto.of(owner.getFirstName(), owner.getLastName(), review);
    }

    public Set<String> getMembersEmails(long id) {
        Set<UserReview> userReviews = getReviewById(id).getUsers();
        return userReviews.stream()
                .map((userReview) -> userReview.getUser().getEmail())
                .collect(Collectors.toSet());
    }

    public Review getReviewById(long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with given id not found"));
    }

    public void removeMember(Review review, User user) {
        review.removeUser(user);
    }

    public void updateReview(long id, ReviewDto reviewDto) {
        Review currentReview = getReviewById(id);
        currentReview.setTitle(reviewDto.getName() == null || reviewDto.getName().isEmpty()
                ? currentReview.getTitle() : reviewDto.getName());
        currentReview.setResearchArea(reviewDto.getResearchArea() == null
                ? currentReview.getResearchArea() : reviewDto.getResearchArea());
        currentReview.setDescription(reviewDto.getDescription() == null
                ? currentReview.getDescription() : reviewDto.getDescription());
        currentReview.setIsPublic(reviewDto.getIsPublic() == null
                ? currentReview.getIsPublic() : reviewDto.getIsPublic());
        currentReview.setScreeningReviewers(reviewDto.getScreeningReviewers() == null
                ? currentReview.getScreeningReviewers() : reviewDto.getScreeningReviewers());
        currentReview.getResearchQuestions().clear();
        reviewDto.getResearchQuestions().forEach(q -> {
            ResearchQuestion researchQuestion = new ResearchQuestion(q);
            researchQuestion.setReview(currentReview);
            currentReview.addResearchQuestion(researchQuestion);
        });
        reviewRepository.save(currentReview);
    }

}
