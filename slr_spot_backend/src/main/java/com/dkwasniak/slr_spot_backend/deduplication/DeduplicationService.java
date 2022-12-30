package com.dkwasniak.slr_spot_backend.deduplication;

import com.dkwasniak.slr_spot_backend.deduplication.dto.DeduplicationDto;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.study.StudyService;
import com.dkwasniak.slr_spot_backend.study.dto.IdentificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DeduplicationService {

    private static final List<DeduplicationType> ALLOWED_DEDUPLICATION_TYPES =
            List.of(DeduplicationType.DOI, DeduplicationType.BASIC_INFORMATION);

    private final StudyService studyService;

    public DeduplicationDto removeDuplicates(Long reviewId, List<Study> studies, DeduplicationType deduplicationType) {
        validateDeduplicationFields(deduplicationType);
        if (DeduplicationType.DOI.equals(deduplicationType)) {
            Set<String> studiesDoi = studyService.getStudiesDoiByReviewId(reviewId);
            return performDoiDeduplication(studies, studiesDoi);
        } else if (DeduplicationType.BASIC_INFORMATION.equals(deduplicationType)) {
            Set<IdentificationDto> studiesIdentification = studyService.getStudyBasicInfoByReviewId(reviewId);
            return performBasicInformationDeduplication(studies, studiesIdentification);
        } else {
            return DeduplicationDto.builder()
                    .correctStudies(studies)
                    .numOfRemovedDuplicates(0)
                    .build();
        }
    }

    private DeduplicationDto performDoiDeduplication(List<Study> studies, Set<String> studiesDoi) {
        int removedDuplicates = 0;
        List<Study> results = new ArrayList<>(studies);
        for (var study : studies) {
            if (studiesDoi.contains(study.getDoi())) {
                results.remove(study);
                removedDuplicates += 1;
            }
        }
        return DeduplicationDto.builder()
                .correctStudies(results)
                .numOfRemovedDuplicates(removedDuplicates)
                .build();
    }

    private DeduplicationDto performBasicInformationDeduplication(List<Study> studies,
                                                                  Set<IdentificationDto> studiesIdentification) {
        int removedDuplicates = 0;
        List<Study> results = new ArrayList<>(studies);
        for (var study : studies) {
            IdentificationDto identifier = studiesIdentification.stream().filter(
                    i -> i.getTitle().equals(study.getTitle())
                            && i.getAuthors().equals(study.getAuthors())
                            && i.getPublicationYear().equals(study.getPublicationYear())
            ).findAny().orElse(null);
            if (identifier != null) {
                results.remove(study);
                removedDuplicates += 1;
            }
        }
        return DeduplicationDto.builder()
                .correctStudies(results)
                .numOfRemovedDuplicates(removedDuplicates)
                .build();
    }

    private void validateDeduplicationFields(DeduplicationType deduplicationType) {
        if (!ALLOWED_DEDUPLICATION_TYPES.contains(deduplicationType)) {
            throw new IllegalStateException("Not allowed deduplication type");
        }
    }
}
