package com.dkwasniak.slr_spot_backend.keyword;

import com.dkwasniak.slr_spot_backend.criterion.CriterionType;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWord;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWordFacade;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWordService;
import com.dkwasniak.slr_spot_backend.keyWord.dto.KeyWordDto;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class KeyWordFacadeTest {

    @InjectMocks
    private KeyWordFacade keyWordFacade;

    @Mock
    private KeyWordService keyWordService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private UserService userService;

    @Test
    public void getKeyWords_shouldReturnReviewKeywords_whenExists() {
        var keywords = Set.of(new KeyWord(), new KeyWord());

        when(keyWordService.getKeyWordsByReviewId(1L)).thenReturn(keywords);
        var criterionList = keyWordFacade.getKeyWords(1L);

        assertEquals(2, criterionList.size());
    }

    @Test
    public void getUserKeyWords_shouldReturnUserReviewKeywords_whenExists() {
        var keywords = Set.of(new KeyWord(), new KeyWord());

        when(keyWordService.getKeyWordsByReviewIdAndUserId(1L, 1L)).thenReturn(keywords);
        var criterionList = keyWordFacade.getKeyWords(1L, 1L);

        assertEquals(2, criterionList.size());
    }

    @Test
    public void addKeyWord_shouldReturnKeyWordId_whenAdded() {
        var keyWord = new KeyWord();
        var keyWordDto = new KeyWordDto(1L, null, "testName", CriterionType.INCLUSION.name());
        keyWord.setId(1L);

        when(reviewService.getReviewById(keyWordDto.getReviewId())).thenReturn(new Review());
        when(keyWordService.saveKeyWord(any())).thenReturn(keyWord.getId());
        var saveKeyWord = keyWordFacade.addKeyWord(keyWordDto);

        assertEquals(1L, saveKeyWord);
    }

    @Test
    public void addUserKeyWord_shouldReturnKeyWordId_whenAdded() {
        var keyWord = new KeyWord();
        var keyWordDto = new KeyWordDto(1L, 1L, "testName", CriterionType.INCLUSION.name());
        keyWord.setId(1L);

        when(reviewService.getReviewById(keyWordDto.getReviewId())).thenReturn(new Review());
        when(keyWordService.saveKeyWord(any())).thenReturn(keyWord.getId());
        when(userService.getUserById(any())).thenReturn(new User());
        var saveKeyWord = keyWordFacade.addKeyWord(keyWordDto);

        assertEquals(1L, saveKeyWord);
    }

    @Test
    public void removeKeyWord() {
        doNothing().when(keyWordService).deleteById(1L);

        keyWordFacade.removeKeyWord(1L);

        verify(keyWordService, times(1)).deleteById(anyLong());
    }
}
