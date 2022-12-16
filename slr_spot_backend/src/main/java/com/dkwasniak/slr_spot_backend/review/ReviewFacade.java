package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.imports.Import;
import com.dkwasniak.slr_spot_backend.imports.ImportService;
import com.dkwasniak.slr_spot_backend.review.dto.AddMembersDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewWithOwnerDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewMemberDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleService;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.tag.TagService;
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
    private final TagService tagService;
    private final ImportService importService;
    private final ReviewRoleService roleService;

    public long createReview(ReviewDto reviewDto, String username) {
        Review review = new Review(reviewDto.getName(), reviewDto.getResearchArea(), reviewDto.getDescription(),
                reviewDto.getIsPublic(), reviewDto.getScreeningReviewers());
        long id = reviewService.saveReview(review);
        User owner = userService.getUserByEmail(username);
        ReviewRole ownerRole = roleService.getRoleByName("OWNER");
        ReviewRole memberRole = roleService.getRoleByName("MEMBER");
        userService.addReviewToUser(owner, review, ownerRole);
        for (String email : reviewDto.getReviewers()) {
            User user = userService.getUserByEmail(email);
            userService.addReviewToUser(user, review, memberRole);
        }
        return id;
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
        ReviewRole memberRole = roleService.getRoleByName("MEMBER");
        for (String email : addMembersDto.getMembersToAdd()) {
            User user = userService.getUserByEmail(email);
            userService.addReviewToUser(user, review, memberRole);
        }
    }

    public Set<Tag> getTags(long id) {
        Review review = reviewService.getReviewById(id);
        return review.getTags();
    }

    public void removeTag(long reviewId, long tagId) {
        Tag tag = tagService.getById(tagId);
        Review review = reviewService.getReviewById(reviewId);
        reviewService.removeTag(review, tag);
    }

    public void addTag(long reviewId, String tagName) {
        if (!tagService.checkIfExistsByName(tagName)) {
            reviewService.addTag(reviewId, new Tag(tagName));
        } else {
            reviewService.addTag(reviewId, tagService.getByName(tagName));
        }
    }

    public void updateReview(long id, ReviewDto reviewDto) {
        reviewService.updateReview(id, reviewDto);
    }

    public Set<Import> getImports(long reviewId) {
        return importService.getImportsByReviewId(reviewId);
    }
}
