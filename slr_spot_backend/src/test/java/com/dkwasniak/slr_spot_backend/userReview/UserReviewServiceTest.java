package com.dkwasniak.slr_spot_backend.userReview;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleEnum;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.userReview.exception.RoleChangeNotAllowedException;
import com.dkwasniak.slr_spot_backend.userReview.exception.UserReviewNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserReviewServiceTest {

    @InjectMocks
    private UserReviewService userReviewService;

    @Mock
    private UserReviewRepository userReviewRepository;

    @Test
    public void changeUserReviewRole_shouldThrowRoleChangeNotAllowedException_whenRoleOwner() {
        var ownerRole = new ReviewRole(ReviewRoleEnum.OWNER.name());
        var userReview = new UserReview(new User(), new Review(), ownerRole);

        assertThrows(RoleChangeNotAllowedException.class,
                () -> userReviewService.changeUserReviewRole(userReview, ownerRole));
    }

    @Test
    public void getUserReviewByReviewId_shouldReturnUserReviews_whenExists() {
        var userReviews = List.of(new UserReview(new User(), new Review(), null));
        var page = new PageImpl<>(userReviews);

        when(userReviewRepository.findByReview_IdOrderByUser_LastName(anyLong(), any())).thenReturn(page);
        var userReviewPage = userReviewService.getUserReviewByReviewId(1L, 1, 1);

        assertEquals(1, userReviewPage.getSize());
    }

    @Test
    public void getUserReviewByReviewIdAndUserId_shouldReturnUserReview_whenExists() {
        var userReview = new UserReview(new User(), new Review(), new ReviewRole(ReviewRoleEnum.MEMBER.name()));

        when(userReviewRepository.findByReview_IdAndUser_Id(1L, 1L)).thenReturn(Optional.of(userReview));
        var userReviewFound = userReviewService.getUserReviewByReviewIdAndUserId(1L, 1L);

        assertEquals("MEMBER", userReviewFound.getRole().getName());
    }

    @Test
    public void getUserReviewByReviewIdAndUserId_shouldThrowUserNotFoundException_whenNotExists() {
        when(userReviewRepository.findByReview_IdAndUser_Id(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(UserReviewNotFoundException.class,
                () -> userReviewService.getUserReviewByReviewIdAndUserId(1L, 1L));
    }
}
