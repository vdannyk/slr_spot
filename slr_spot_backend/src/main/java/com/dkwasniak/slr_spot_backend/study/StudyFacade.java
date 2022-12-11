package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.file.FileService;
import com.dkwasniak.slr_spot_backend.imports.Import;
import com.dkwasniak.slr_spot_backend.imports.ImportService;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.study.mapper.StudyMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class StudyFacade {

    private final StudyService studyService;
    private final FileService fileService;
    private final ImportService importService;
    private final ReviewService reviewService;

    public List<Study> getStudiesByReviewId(Long reviewId) {
        Set<Import> imports = importService.getImportsByReviewId(reviewId);
        List<Study> studies = new ArrayList<>();
        for (var studyImport : imports) {
            studies.addAll(studyImport.getStudies());
        }
        return studies;
    }
}
