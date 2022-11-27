package com.dkwasniak.slr_spot_backend.keyWord;

import com.dkwasniak.slr_spot_backend.criterion.CriterionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public KeyWord getByIdAndType(long keywordId, CriterionType type) {
        return keywordRepository.findByIdAndType(keywordId, type).orElseThrow();
    }
}
