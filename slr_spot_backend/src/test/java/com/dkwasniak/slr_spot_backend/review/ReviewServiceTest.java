package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.dkwasniak.slr_spot_backend.review.exception.ReviewNotFoundException;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Test
    public void getReviewById_shouldThrowReviewNotFoundException_whenNotExist() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class,
                () -> reviewService.getReviewById(1L));
    }

    @Test
    public void getPublicReviews_shouldReturnPublicReviews() {
        var review1 = new Review();
        var review2 = new Review();
        List<Review> reviews = new ArrayList<>() {{
            add(review1);
            add(review2);
        }};

        when(reviewRepository.findAllPublic(any())).thenReturn(new PageImpl<>(reviews));
        var reviewPage = reviewService.getPublicReviews(0, 10);

        assertEquals(2, reviewPage.getContent().size());
    }

    @Test
    public void getReviewsByUser_shouldReturnUserReviews() {
        var review1 = new Review();
        var review2 = new Review();
        List<Review> reviews = new ArrayList<>() {{
            add(review1);
            add(review2);
        }};

        when(reviewRepository.findByUsers_User_Id(anyLong(), any())).thenReturn(new PageImpl<>(reviews));
        var reviewPage = reviewService.getReviewsByUser(1L, 0, 10);

        assertEquals(2, reviewPage.getContent().size());
    }

    @Test
    public void toReviewsPageDto_shouldMapReviewsToDto() {
        var review1 = new Review();
        var user = new User("test", "test", "test@gmail.com", "123");
        review1.setUsers(Set.of(new UserReview(user, review1, new ReviewRole("OWNER"))));
        List<Review> reviews = new ArrayList<>() {{
            add(review1);
        }};
        Pageable pageRq = PageRequest.of(0, 10);
        var page = new PageImpl<>(reviews, pageRq, 10);

        var reviewsPageDto = reviewService.toReviewsPageDto(page);

        assertEquals(1, reviewsPageDto.getReviews().size());
    }

    @Test
    public void updateReview_shouldUpdateReviewFields_whenDtoContainsInfo() {
        var review1 = new Review();
        var reviewDto = ReviewDto.builder().isPublic(true).name("test").researchQuestions(new ArrayList<>()).build();

        when(reviewRepository.findById(anyLong())).thenReturn(java.util.Optional.of(review1));
        when(reviewRepository.save(any())).thenReturn(review1);

        reviewService.updateReview(1L, reviewDto);

        assertEquals("test", review1.getTitle());
        assertEquals(true, review1.getIsPublic());
    }
}
