package com.dkwasniak.slr_spot_backend.deduplication;

import com.dkwasniak.slr_spot_backend.deduplication.dto.DeduplicationDto;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.study.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DeduplicationService {

    private static final List<String> DEDUPLICATION_ATTRIBUTES = List.of("DOI", "TITLE", "AUTHORS");
    private static final String DOI = "DOI";
    private static final String TITLE = "TITLE";
    private static final String AUTHORS = "AUTHORS";

    private final StudyService studyService;

    public DeduplicationDto removeDuplicates(Long reviewId, List<Study> studies, String[] selectedDeduplicationFields) {
        validateDeduplicationFields(selectedDeduplicationFields);
        List<String> studiesDoi = studyService.getStudiesDoiByReviewId(reviewId);
        List<String> studiesTitles = studyService.getStudiesTitlesByReviewId(reviewId);
        List<String> studiesAuthors = studyService.getStudiesAuthorsByReviewId(reviewId);
        Map<String, Boolean> deduplicationMap = new HashMap<>() {{
            put(DOI, false);
            put(TITLE, false);
            put(AUTHORS, false);
        }};

        int removedDuplicates = 0;
        List<Study> results = new ArrayList<>(studies);
        for (var study : studies) {
            deduplicationMap.put(DOI, checkIfDuplicateByDoi(study, studiesDoi));
            deduplicationMap.put(TITLE, checkIfDuplicateByTitle(study, studiesTitles));
            deduplicationMap.put(AUTHORS, checkIfDuplicateByAuthors(study, studiesAuthors));

            if (isDuplicate(deduplicationMap, selectedDeduplicationFields)) {
                results.remove(study);
                removedDuplicates += 1;
            }
        }

        return DeduplicationDto.builder()
                .correctStudies(results)
                .numOfRemovedDuplicates(removedDuplicates)
                .build();
    }

    private boolean isDuplicate(Map<String, Boolean> deduplicationMap, String[] selectedDeduplicationFields) {
        int confirmedFields = 0;
        for (var field : selectedDeduplicationFields) {
            if (deduplicationMap.get(field)) {
                confirmedFields += 1;
            }
        }
        return confirmedFields == selectedDeduplicationFields.length;
    }

    private boolean checkIfDuplicateByDoi(Study study, List<String> studiesDoi) {
        return studiesDoi.contains(study.getDoi());
    }

    private boolean checkIfDuplicateByTitle(Study study, List<String> studiesTitles) {
        return studiesTitles.contains(study.getTitle());
    }

    private boolean checkIfDuplicateByAuthors(Study study, List<String> studiesAuthors) {
        return studiesAuthors.contains(study.getAuthors());
    }

    private void validateDeduplicationFields(String[] selectedDeduplicationFields) {
        for (var field : selectedDeduplicationFields) {
            if (!DEDUPLICATION_ATTRIBUTES.contains(field)) {
                throw new IllegalStateException("Deduplication for this field is not allowed");
            }
        }
    }
}
