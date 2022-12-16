package com.dkwasniak.slr_spot_backend.keyWord;

import com.dkwasniak.slr_spot_backend.criterion.CriterionType;
import com.dkwasniak.slr_spot_backend.keyWord.dto.KeyWordDto;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class KeyWordFacade {

    private final KeyWordService keyWordService;
    private final ReviewService reviewService;
    private final UserService userService;

    public Set<KeyWord> getKeyWords(Long reviewId) {
        return keyWordService.getKeyWordsByReviewId(reviewId);
    }

    public Set<KeyWord> getKeyWords(Long reviewId, Long userId) {
        return keyWordService.getKeyWordsByReviewIdAndUserId(reviewId, userId);
    }

    public Long addKeyWord(KeyWordDto keyWordDto) {
        Review review = reviewService.getReviewById(keyWordDto.getReviewId());
        KeyWord keyWord = new KeyWord(keyWordDto.getName(), CriterionType.valueOf(keyWordDto.getType()));
        keyWord.setReview(review);
        if (keyWordDto.getUserId() != null) {
            User user = userService.getUserById(keyWordDto.getUserId());
            keyWord.setUser(user);
        }
        return keyWordService.saveKeyWord(keyWord);
    }

    public void removeKeyWord(Long keyWordId) {
        keyWordService.deleteById(keyWordId);
    }
}
