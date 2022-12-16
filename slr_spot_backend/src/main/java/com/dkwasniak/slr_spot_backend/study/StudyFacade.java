package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.file.FileService;
import com.dkwasniak.slr_spot_backend.imports.Import;
import com.dkwasniak.slr_spot_backend.imports.ImportService;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
import com.dkwasniak.slr_spot_backend.study.mapper.StudyMapper;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class StudyFacade {

    private final StudyService studyService;
    private final FileService fileService;
    private final ImportService importService;
    private final ReviewService reviewService;
    private final UserService userService;

    public List<Study> getStudiesByReviewId(Long reviewId) {
        Set<Import> imports = importService.getImportsByReviewId(reviewId);
        List<Study> studies = new ArrayList<>();
        for (var studyImport : imports) {
            studies.addAll(studyImport.getStudies());
        }
        return studies;
    }

    public List<Study> getStudiesToBeReviewed(Long reviewId, Long userId) {
        Review review = reviewService.getReviewById(reviewId);
        User user = userService.getUserById(userId);
        List<Study> studies = studyService.getStudiesToBeReviewed(review, user);
        return studies;
    }

}
