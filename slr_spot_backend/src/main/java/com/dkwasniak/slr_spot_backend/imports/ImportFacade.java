package com.dkwasniak.slr_spot_backend.imports;

import com.dkwasniak.slr_spot_backend.deduplication.DeduplicationService;
import com.dkwasniak.slr_spot_backend.deduplication.dto.DeduplicationDto;
import com.dkwasniak.slr_spot_backend.file.FileService;
import com.dkwasniak.slr_spot_backend.imports.dto.ImportDto;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.study.StudyService;
import com.dkwasniak.slr_spot_backend.study.exception.StudyMappingException;
import com.dkwasniak.slr_spot_backend.study.exception.StudyMappingInvalidHeadersException;
import com.dkwasniak.slr_spot_backend.study.mapper.StudyMapper;
import com.dkwasniak.slr_spot_backend.study.status.StatusEnum;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportFacade {

    private final FileService fileService;
    private final ImportService importService;
    private final ReviewService reviewService;
    private final ImportRepository importRepository;
    private final DeduplicationService deduplicationService;
    private final UserService userService;

    @Transactional
    public void importStudies(ImportContext importContext) {
        List<Study> studies = loadFromFile(importContext.getFile(), importContext.getSource());
        DeduplicationDto deduplicationDto = deduplicationService.removeDuplicates(importContext.getReviewId(),
                studies, importContext.getDeduplicationFields());
        studies = deduplicationDto.getCorrectStudies();

        Review review = reviewService.getReviewById(importContext.getReviewId());
        review.setNumOfRemovedDuplicates(
                review.getNumOfRemovedDuplicates() + deduplicationDto.getNumOfRemovedDuplicates()
        );

        User user = userService.getUserById(importContext.getUserId());
        Import studyImport = new Import(
                importContext.getSearchValue(),
                importContext.getSource(),
                importContext.getAdditionalInfo(),
                user.getEmail(),
                deduplicationDto.getNumOfRemovedDuplicates()
        );
        studyImport.setReview(review);
        studyImport.setStudies(studies);
        studies.forEach(s -> {
            s.setStudyImport(studyImport);
            s.setStatus(StatusEnum.TITLE_ABSTRACT);
//            s.addOperation(new Operation("Import performed"));
        });
        importRepository.save(studyImport);
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
        try {
            List<CSVRecord> records = fileService.loadFromCsv(file);
            return StudyMapper.csvToStudies(records, source);
        } catch (IllegalArgumentException e) {
            log.error("Exception while mapping studies", e);
            throw new StudyMappingInvalidHeadersException(file.getName(), e);
        } catch (Exception e) {
            throw new StudyMappingException(file.getName(), e);
        }
    }

    private List<Study> loadStudiesFromBib(MultipartFile file) {
        try {
            BibTeXDatabase records = fileService.loadFromBibtex(file);
            return StudyMapper.bibToStudies(records);
        } catch (Exception e) {
            log.error("Exception while mapping studies", e);
            throw new StudyMappingException(file.getName(), e);
        }
    }

    public void removeImportById(Long importId) {
        importService.deleteImportById(importId);
    }

    public Set<ImportDto> getImportsByReviewId(Long reviewId) {
        Set<Import> imports = reviewService.getReviewById(reviewId).getImports();
        return imports.stream()
                .map((i) -> new ImportDto(i, i.getStudies().size()))
                .collect(Collectors.toSet());
    }
}
