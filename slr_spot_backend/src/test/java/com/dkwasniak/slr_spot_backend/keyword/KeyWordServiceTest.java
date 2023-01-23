package com.dkwasniak.slr_spot_backend.keyword;

import com.dkwasniak.slr_spot_backend.keyWord.KeyWord;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWordRepository;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWordService;
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
public class KeyWordServiceTest {

    @InjectMocks
    private KeyWordService keyWordService;

    @Mock
    private KeyWordRepository keyWordRepository;

    @Test
    public void getKeyWordsByReviewId_shouldReturnReviewKeywords_whenExists() {
        var keywords = Set.of(new KeyWord(), new KeyWord());

        when(keyWordRepository.findByReview_IdAndUserNull(1L)).thenReturn(keywords);
        var keyWords = keyWordService.getKeyWordsByReviewId(1L);

        assertEquals(2, keyWords.size());
    }

    @Test
    public void getKeyWordsByReviewIdAndUserId_shouldReturnReviewKeywords_whenExists() {
        var keywords = Set.of(new KeyWord(), new KeyWord());

        when(keyWordRepository.findByReview_IdAndUser_Id(1L, 1L)).thenReturn(keywords);
        var keyWords = keyWordService.getKeyWordsByReviewIdAndUserId(1L, 1L);

        assertEquals(2, keyWords.size());
    }

    @Test
    public void saveKeyWord_shouldReturnKeywordId_whenSaved() {
        var keyword = new KeyWord();
        keyword.setId(1L);

        when(keyWordRepository.save(any())).thenReturn(keyword);
        var saveKeyWord = keyWordService.saveKeyWord(keyword);

        assertEquals(1L, saveKeyWord);
    }

    @Test
    public void removeKeywordById_shouldRemoveKeyWord() {
        doNothing().when(keyWordRepository).deleteById(anyLong());
        keyWordService.deleteById(1L);

        verify(keyWordRepository, times(1)).deleteById(anyLong());
    }
}
