package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.criterion.Criterion;
import com.dkwasniak.slr_spot_backend.criterion.CriterionService;
import com.dkwasniak.slr_spot_backend.criterion.CriterionType;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWord;
import com.dkwasniak.slr_spot_backend.keyWord.KeywordService;
import com.dkwasniak.slr_spot_backend.review.dto.AddMembersDto;
import com.dkwasniak.slr_spot_backend.review.dto.NewReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewMemberDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
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
    private final CriterionService criterionService;
    private final KeywordService keywordService;

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

    public Set<Criterion> getCriteria(long id) {
        Review review = reviewService.getReviewById(id);
        return review.getCriteria();
    }

    public Set<KeyWord> getKeywords(long id) {
        Review review = reviewService.getReviewById(id);
        return review.getKeywords();
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

    public void addCriterion(long reviewId, String criterionName, String type) {
        CriterionType criterionType = criterionService.getTypeByName(type);
        if (!criterionService.checkIfExistByNameAndTypeName(criterionName, type)) {
            reviewService.addCriterion(reviewId, new Criterion(criterionName, criterionType));
        } else {
            reviewService.addCriterion(reviewId, criterionService.getByNameAndType(criterionName, criterionType));
        }
    }

    public void removeCriterion(long reviewId, long criterionId, long criterionTypeId) {
        CriterionType criterionType = criterionService.getTypeById(criterionTypeId);
        Criterion criterion = criterionService.getByIdAndType(criterionId, criterionType);
        Review review = reviewService.getReviewById(reviewId);
        reviewService.removeCriterion(review, criterion);
    }

    public void addKeyword(long reviewId, String keywordName, String type) {
        CriterionType criterionType = criterionService.getTypeByName(type);
        if (!keywordService.checkIfExistByNameAndTypeName(keywordName, type)) {
            reviewService.addKeyword(reviewId, new KeyWord(keywordName, criterionType));
        } else {
            reviewService.addKeyword(reviewId, keywordService.getByNameAndType(keywordName, criterionType));
        }
    }

    public void removeKeyword(long reviewId, long keywordId, long keywordTypeId) {
        CriterionType criterionType = criterionService.getTypeById(keywordTypeId);
        KeyWord keyWord = keywordService.getByIdAndType(keywordId, criterionType);
        Review review = reviewService.getReviewById(reviewId);
        reviewService.removeKeyword(review, keyWord);
    }

}
