package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewWithOwnerDto;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewsPageDto;
import com.dkwasniak.slr_spot_backend.review.report.ReportFactory;
import com.dkwasniak.slr_spot_backend.review.researchQuestion.ResearchQuestion;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleService;
import com.dkwasniak.slr_spot_backend.study.StudyService;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import com.dkwasniak.slr_spot_backend.userReview.UserReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ReviewFacadeTest {

    @InjectMocks
    private ReviewFacade reviewFacade;

    @Mock
    private ReviewService reviewService;
    @Mock
    private UserService userService;
    @Mock
    private ReviewRoleService roleService;
    @Mock
    private UserReviewService userReviewService;
    @Mock
    private ReportFactory reportFactory;
    @Mock
    private StudyService studyService;

    @Test
    public void getPublicReviews_shouldReturnPublicReviews() {
        var review1 = new Review();
        var review2 = new Review();
        List<Review> reviews = new ArrayList<>() {{
            add(review1);
            add(review2);
        }};

        when(reviewService.getPublicReviews(anyInt(), anyInt())).thenReturn(new PageImpl<>(reviews));
        when(reviewService.toReviewsPageDto(any())).thenReturn(ReviewsPageDto.builder()
                        .reviews(Set.of(ReviewWithOwnerDto.of("test", "test", review1),
                                ReviewWithOwnerDto.of("test2", "test2", review2)))
                .build());
        var reviewPage = reviewFacade.getPublicReviews(0, 10);

        assertEquals(2, reviewPage.getReviews().size());
    }

    @Test
    public void getReviewsByUser_shouldReturnUserReviews() {
        var review1 = new Review();
        var review2 = new Review();
        List<Review> reviews = new ArrayList<>() {{
            add(review1);
            add(review2);
        }};

        when(reviewService.getReviewsByUser(anyLong(), anyInt(), anyInt())).thenReturn(new PageImpl<>(reviews));
        when(reviewService.toReviewsPageDto(any())).thenReturn(ReviewsPageDto.builder()
                .reviews(Set.of(ReviewWithOwnerDto.of("test", "test", review1),
                        ReviewWithOwnerDto.of("test2", "test2", review2)))
                .build());
        var reviewPage = reviewFacade.getReviewsByUserId(1L, 0, 10);

        assertEquals(2, reviewPage.getReviews().size());
    }

    @Test
    public void addReview() {
        var user = new User("test", "test", "test@gmail.com", "123");
        var reviewDto = ReviewDto.builder()
                .userId(1L)
                .researchArea("researchArea")
                .isPublic(true)
                .name("test")
                .researchQuestions(new ArrayList<>())
                .description("description")
                .screeningReviewers(2)
                .reviewers(new HashSet<>()).build();

        when(userService.getUserById(anyLong())).thenReturn(user);
        when(roleService.getRoleByName(anyString())).thenReturn(new ReviewRole("OWNER")).thenReturn(new ReviewRole("MEMBER"));
        when(reviewService.saveReview(any())).thenReturn(1L);
        var newReviewId = reviewFacade.addReview(reviewDto);

        assertEquals(1L, newReviewId);
    }

    @Test
    public void getUsersAvailableToAdd_shouldReturnEmailsOfUserNotInReview() {
        when(userService.getAllEmails()).thenReturn(new HashSet<>(){{ add("test@gmail.com"); add("testowo@gmail.com");}});
        when(reviewService.getMembersEmails(anyLong())).thenReturn(new HashSet<>(){{ add("test@gmail.com"); }});

        var usersAvailableToAdd = reviewFacade.getUsersAvailableToAdd(1L);

        assertEquals(1, usersAvailableToAdd.size());
        assertTrue(usersAvailableToAdd.contains("testowo@gmail.com"));
    }

    @Test
    public void generateReviewReport() {
        var review = new Review();
        var user = new User("test", "test", "test@gmail.com", "123");
        review.setTitle("title");
        review.setUsers(Set.of(new UserReview(user, review, new ReviewRole("OWNER"))));
        review.setResearchArea("researchArea");
        review.setDescription("description");
        review.setResearchQuestions(Set.of(new ResearchQuestion("research question")));
        review.setNumOfImportedStudies(4);
        review.setNumOfRemovedDuplicates(2);

        when(reviewService.getReviewById(anyLong())).thenReturn(review);
        when(reportFactory.createReport(any())).thenReturn(new InputStreamResource(new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray())));
        when(studyService.getStudiesCountByState(anyLong(), any())).thenReturn(2);

        var resource = reviewFacade.generateReviewReport(1L);

        assertNotNull(resource);
    }

    @Test
    public void getReviewById_shouldReturnReviewBoundWithOwner() {
        var review = new Review();
        var user = new User("test", "test", "test@gmail.com", "123");
        review.setTitle("title");
        review.setUsers(Set.of(new UserReview(user, review, new ReviewRole("OWNER"))));
        review.setResearchArea("researchArea");
        review.setDescription("description");
        review.setResearchQuestions(Set.of(new ResearchQuestion("research question")));
        review.setNumOfImportedStudies(4);
        review.setNumOfRemovedDuplicates(2);

        when(reviewService.getReviewById(anyLong())).thenReturn(review);
        when(reviewService.boundWithOwner(any())).thenReturn(ReviewWithOwnerDto.of("Test", "Testowy", review));
        var reviewWithOwnerDto = reviewFacade.getReviewById(1L);

        assertNotNull(reviewWithOwnerDto);
        assertEquals("title", reviewWithOwnerDto.getReview().getTitle());
        assertEquals("Test", reviewWithOwnerDto.getFirstName());
    }

    @Test
    public void getMemberRole_shouldReturnMemberReviewRole() {
        when(userReviewService.getUserReviewByReviewIdAndUserId(anyLong(), anyLong())).thenReturn(
                new UserReview(new User(), new Review(), new ReviewRole("OWNER"))
        );

        var memberRole = reviewFacade.getMemberRole(1L, 1L);

        assertEquals("OWNER", memberRole);
    }
}
