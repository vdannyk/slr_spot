package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.AddMembersDto;
import com.dkwasniak.slr_spot_backend.review.dto.NewReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewMemberDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

@Component
@Transactional
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewService reviewService;
    private final UserService userService;

    public long createReview(NewReviewDto newReviewDto, String username) {
        Review review = new Review(newReviewDto.getName(), newReviewDto.getResearchArea(), newReviewDto.getDescription(),
                newReviewDto.getIsPublic(), newReviewDto.getScreeningReviewers());
        long id = reviewService.saveReview(review);
        User owner = userService.getUserByEmail(username);
        userService.addReviewToUser(owner, review, ReviewRole.OWNER);
        for (String email : newReviewDto.getReviewers()) {
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

    public ReviewDto getReviewById(long id) {
        return reviewService.boundWithOwner(reviewService.getReviewById(id));
    }

    public Set<ReviewMemberDto> getMembers(long id) {
        return reviewService.getMembers(id);
    }

    public ReviewsPageDto getReviewsByUser(long userId, int page, int size) {
        return reviewService.toReviewsPageDto(
                reviewService.getReviewsByUser(userId, page, size)
        );
    }

    public void removeMember(long reviewId, long userId) {
        reviewService.removeMember(reviewId, userId);
    }

    public Set<String> getUsersAvailableToAdd(long reviewId) {
        Set<String> allAvailableEmails = userService.getAllEmails();
        Set<String> currentMembers = reviewService.getMembersEmails(reviewId);
        allAvailableEmails.removeAll(currentMembers);
        return allAvailableEmails;
    }

    public void addMembers(long reviewId, AddMembersDto addMembersDto) {
        Review review = reviewService.getReviewById(reviewId);
        for (String email : addMembersDto.getMembersToAdd()) {
            User user = userService.getUserByEmail(email);
            userService.addReviewToUser(user, review);
        }
    }

    public Set<Tag> getTags(long id) {
        Review review = reviewService.getReviewById(id);
        return review.getTags();
    }
}
