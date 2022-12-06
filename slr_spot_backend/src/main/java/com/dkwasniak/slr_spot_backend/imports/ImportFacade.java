package com.dkwasniak.slr_spot_backend.imports;

import com.dkwasniak.slr_spot_backend.file.FileService;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.study.StudyService;
import com.dkwasniak.slr_spot_backend.study.mapper.StudyMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImportFacade {

    private final StudyService studyService;
    private final FileService fileService;
    private final ImportService importService;
    private final ReviewService reviewService;
    private final ImportRepository importRepository;

    public void importStudies(MultipartFile file, Long reviewId, String source, String searchValue, String additionalInfo) {
        List<Study> studies = loadFromFile(file, source);
        Review review = reviewService.getReviewById(reviewId);
        Import studyImport = new Import(
                searchValue, source, additionalInfo, "testPerformer"
        );
        studyImport.setReview(review);
        studyImport.setStudies(studies);
        for (var study : studies) {
            study.setStudyImport(studyImport);
        }
        importRepository.save(studyImport);
//        reviewService.addImport(reviewId, studyImport);
    }

    private List<Study> loadFromFile(MultipartFile file, String source) {
        String contentType = file.getContentType();
        fileService.checkIfAllowedFileContentType(contentType);
        List<Study> studies;
        if (fileService.isCsvFile(contentType)) {
            studies = loadStudiesFromCsv(file, source);
        } else if (fileService.isBibtexFile(contentType)) {
            studies = loadStudiesFromBib(file);
        } else if (fileService.isRisFile(contentType)){
            studies = new ArrayList<>();
        } else if (fileService.isXlsFile(contentType)) {
            studies = new ArrayList<>();
        } else {
            throw new IllegalStateException();
        }
        return studies;
    }

    private List<Study> loadStudiesFromCsv(MultipartFile file, String source) {
        List<CSVRecord> records = fileService.loadFromCsv(file);
        return StudyMapper.csvToStudies(records, source);
    }

    private List<Study> loadStudiesFromBib(MultipartFile file) {
        BibTeXDatabase records = fileService.loadFromBibtex(file);
        return StudyMapper.bibToStudies(records);
    }

    public void removeImportById(Long importId) {
        importService.deleteImportById(importId);
    }
}
