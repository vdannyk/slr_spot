package com.dkwasniak.slr_spot_backend.keyWord;

import com.dkwasniak.slr_spot_backend.criterion.CriterionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public boolean checkIfExistByNameAndTypeName(String keywordName, String typeName) {
        return keywordRepository.existsByNameAndType_Name(keywordName, typeName);
    }

    public KeyWord getByNameAndType(String keywordName, CriterionType type) {
        return keywordRepository.findByNameAndType(keywordName, type).orElseThrow();
    }

    public KeyWord getById(long id) {
        return keywordRepository.findById(id).orElseThrow();
    }

    public KeyWord getByIdAndType(long keywordId, CriterionType type) {
        return keywordRepository.findByIdAndType(keywordId, type).orElseThrow();
    }

    public Set<KeyWord> getByReviewId(long reviewId) {
        return keywordRepository.findByReview_IdAndUserNull(reviewId);
    }

    public Set<KeyWord> getByReviewIdAndUserId(long reviewId, long userId) {
        return keywordRepository.findByReview_IdAndUser_Id(reviewId, userId);
    }

    public void deleteById(long id) {
        keywordRepository.deleteById(id);
    }
}
