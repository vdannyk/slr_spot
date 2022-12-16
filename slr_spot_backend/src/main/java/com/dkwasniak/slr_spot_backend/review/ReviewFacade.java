package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.imports.Import;
import com.dkwasniak.slr_spot_backend.imports.ImportService;
import com.dkwasniak.slr_spot_backend.review.dto.AddMembersDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewWithOwnerDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewMemberDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleEnum;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleService;
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
    private final ImportService importService;
    private final ReviewRoleService roleService;

    public long addReview(ReviewDto reviewDto) {
        User owner = userService.getUserById(reviewDto.getUserId());
        Review review = new Review(reviewDto.getName(), reviewDto.getResearchArea(), reviewDto.getDescription(),
                reviewDto.getIsPublic(), reviewDto.getScreeningReviewers());
        ReviewRole ownerRole = roleService.getRoleByName(ReviewRoleEnum.OWNER.name());
        ReviewRole memberRole = roleService.getRoleByName(ReviewRoleEnum.MEMBER.name());
        review.addUser(owner, ownerRole);
        reviewDto.getReviewers().forEach(email -> {
            User user = userService.getUserByEmail(email);
            review.addUser(user, memberRole);
        });
        return reviewService.saveReview(review);
    }

    public ReviewsPageDto getPublicReviews(int page, int size) {
        return reviewService.toReviewsPageDto(
                reviewService.getPublicReviews(page, size)
        );
    }

    public ReviewWithOwnerDto getReviewById(long id) {
        return reviewService.boundWithOwner(reviewService.getReviewById(id));
    }

    public Set<ReviewMemberDto> getMembers(long id) {
        return reviewService.getMembers(id);
    }

    public ReviewsPageDto getReviewsByUserId(long userId, int page, int size) {
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
        ReviewRole memberRole = roleService.getRoleByName("MEMBER");
        for (String email : addMembersDto.getMembersToAdd()) {
            User user = userService.getUserByEmail(email);
            userService.addReviewToUser(user, review, memberRole);
        }
    }

    public void updateReview(long id, ReviewDto reviewDto) {
        reviewService.updateReview(id, reviewDto);
    }

    public Set<Import> getImports(long reviewId) {
        return importService.getImportsByReviewId(reviewId);
    }
}
