package com.dkwasniak.slr_spot_backend.imports;

import com.dkwasniak.slr_spot_backend.study.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportRepository importRepository;

    public void saveImport(Import studyImport) {
        importRepository.save(studyImport);
    }

    public Set<Import> getImportsByReviewId(long reviewId) {
        return importRepository.findByReview_Id(reviewId);
    }

}
