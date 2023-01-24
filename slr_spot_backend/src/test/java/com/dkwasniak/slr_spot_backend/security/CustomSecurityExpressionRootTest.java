package com.dkwasniak.slr_spot_backend.security;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserPrincipal;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CustomSecurityExpressionRootTest {

    private CustomSecurityExpressionRoot securityExpressionRoot;

    @Mock
    private ReviewService reviewService;
    @Mock
    private Authentication authentication;

    @BeforeEach
    void init() {
        securityExpressionRoot = new CustomSecurityExpressionRoot(
                authentication,
                reviewService
        );
    }

    @Test
    public void hasFullAccess_shouldReturnTrue_whenOwner() {
        var user = new User("test", "test", "test@gmail.com", "123");
        var review = new Review();
        review.setId(1L);
        user.setReviews(Set.of(new UserReview(user, review, new ReviewRole("OWNER"))));
        when(authentication.getPrincipal()).thenReturn(new UserPrincipal(user));
        var hasAccess = securityExpressionRoot.hasFullAccess(1L);

        assertTrue(hasAccess);
    }

    @Test
    public void hasFullAccess_shouldReturnFalse_whenUserNotInReview() {
        var user = new User("test", "test", "test@gmail.com", "123");
        when(authentication.getPrincipal()).thenReturn(new UserPrincipal(user));
        var hasAccess = securityExpressionRoot.hasFullAccess(1L);

        assertFalse(hasAccess);
    }

    @Test
    public void hasScreeningAccess_shouldReturnTrue_whenOwner() {
        var user = new User("test", "test", "test@gmail.com", "123");
        var review = new Review();
        review.setId(1L);
        user.setReviews(Set.of(new UserReview(user, review, new ReviewRole("OWNER"))));
        when(authentication.getPrincipal()).thenReturn(new UserPrincipal(user));
        var hasAccess = securityExpressionRoot.hasScreeningAccess(1L);

        assertTrue(hasAccess);
    }

    @Test
    public void hasScreeningAccess_shouldReturnFalse_whenUserNotInReview() {
        var user = new User("test", "test", "test@gmail.com", "123");
        when(authentication.getPrincipal()).thenReturn(new UserPrincipal(user));
        var hasAccess = securityExpressionRoot.hasScreeningAccess(1L);

        assertFalse(hasAccess);
    }

    @Test
    public void hasViewAccess_shouldReturnTrue_whenReviewPublic() {
        var review = new Review();
        review.setIsPublic(true);
        when(reviewService.getReviewById(anyLong())).thenReturn(review);
        var hasAccess = securityExpressionRoot.hasViewAccess(1L);

        assertTrue(hasAccess);
    }

    @Test
    public void hasViewAccess_shouldReturnTrue_whenReviewNotPublicButMember() {
        var user = new User("test", "test", "test@gmail.com", "123");
        var review = new Review();
        review.setIsPublic(false);
        review.setId(1L);
        user.setReviews(Set.of(new UserReview(user, review, new ReviewRole("MEMBER"))));
        when(authentication.getPrincipal()).thenReturn(new UserPrincipal(user));
        when(reviewService.getReviewById(anyLong())).thenReturn(review);
        var hasAccess = securityExpressionRoot.hasViewAccess(1L);

        assertTrue(hasAccess);
    }
}
