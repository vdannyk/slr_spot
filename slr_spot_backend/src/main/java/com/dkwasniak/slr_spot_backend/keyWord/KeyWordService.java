package com.dkwasniak.slr_spot_backend.keyWord;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class KeyWordService {

    private final KeyWordRepository keywordRepository;

    public Set<KeyWord> getKeyWordsByReviewId(long reviewId) {
        return keywordRepository.findByReview_IdAndUserNull(reviewId);
    }

    public Set<KeyWord> getKeyWordsByReviewIdAndUserId(long reviewId, long userId) {
        return keywordRepository.findByReview_IdAndUser_Id(reviewId, userId);
    }

    public Long saveKeyWord(KeyWord keyWord) {
        return keywordRepository.save(keyWord).getId();
    }

    public void deleteById(long id) {
        keywordRepository.deleteById(id);
    }
}
