package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.imports.ImportService;
import com.dkwasniak.slr_spot_backend.researchQuestion.ResearchQuestion;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewMembersDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewWithOwnerDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.review.report.ReportData;
import com.dkwasniak.slr_spot_backend.review.report.ReportFactory;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleEnum;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleService;
import com.dkwasniak.slr_spot_backend.study.StudyService;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import com.dkwasniak.slr_spot_backend.userReview.UserReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ImportService importService;
    private final ReviewRoleService roleService;
    private final UserReviewService userReviewService;
    private final ReportFactory reportFactory;
    private final StudyService studyService;

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
        reviewDto.getResearchQuestions().forEach(question -> {
            ResearchQuestion researchQuestion = new ResearchQuestion(question);
            researchQuestion.setReview(review);
            review.addResearchQuestion(researchQuestion);
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

    public Boolean getIsPublic(Long reviewId) {
        return reviewService.getReviewById(reviewId).getIsPublic();
    }

    public ReviewsPageDto getReviewsByUserId(long userId, int page, int size) {
        return reviewService.toReviewsPageDto(
                reviewService.getReviewsByUser(userId, page, size)
        );
    }

    public void removeMember(long reviewId, long userId) {
        User user = userService.getUserById(userId);
        Review review = reviewService.getReviewById(reviewId);
        reviewService.removeMember(review, user);
    }

    public Set<String> getUsersAvailableToAdd(long reviewId) {
        Set<String> allAvailableEmails = userService.getAllEmails();
        Set<String> currentMembers = reviewService.getMembersEmails(reviewId);
        allAvailableEmails.removeAll(currentMembers);
        return allAvailableEmails;
    }

    public void addMembers(long reviewId, ReviewMembersDto reviewMembersDto) {
        Review review = reviewService.getReviewById(reviewId);
        ReviewRole memberRole = roleService.getRoleByName(ReviewRoleEnum.MEMBER.name());
        for (String email : reviewMembersDto.getMembersToAdd()) {
            User user = userService.getUserByEmail(email);
            review.addUser(user, memberRole);
        }
    }

    public void updateReview(long id, ReviewDto reviewDto) {
        reviewService.updateReview(id, reviewDto);
    }

    public String getMemberRole(Long reviewId, Long userId) {
        return userReviewService.getUserReviewByReviewIdAndUserId(reviewId, userId).getRole().getName();
    }

    public InputStreamResource generateReviewReport(Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        return reportFactory.createReport(
                ReportData.builder()
                        .title(review.getTitle())
                        .owner(review.getUsers().stream().filter(u -> ReviewRoleEnum.OWNER.name().equals(u.getRole().getName())).map(u -> u.getUser().getFirstName() + " " + u.getUser().getLastName()).findFirst().orElseThrow())
                        .researchArea(review.getResearchArea())
                        .description(review.getDescription())
                        .researchQuestions(review.getResearchQuestions().stream().map(ResearchQuestion::getName).collect(Collectors.toList()))
                        .members(review.getUsers()
                                .stream()
                                .filter(u -> !ReviewRoleEnum.OWNER.name().equals(u.getRole().getName())).map(u -> u.getUser().getFirstName() + " " + u.getUser().getLastName() + " - " + u.getRole().getName()).collect(Collectors.toList()))
                        .totalStudiesImported(0)
                        .removedDuplicates(review.getNumOfRemovedDuplicates())
//                        .selectedStudies(studyService.getStudiesCountByStatus(reviewId, StatusEnum.INCLUDED))
                .build());
    }

}
