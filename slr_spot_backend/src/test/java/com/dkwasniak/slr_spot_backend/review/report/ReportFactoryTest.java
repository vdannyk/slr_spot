package com.dkwasniak.slr_spot_backend.review.report;


import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.researchQuestion.ResearchQuestion;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleEnum;
import com.dkwasniak.slr_spot_backend.study.StudyState;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ReportFactoryTest {

    @InjectMocks
    private ReportFactory reportFactory;

    @Test
    public void createReport() {
        var reportData = ReportData.builder()
                .title("title")
                .owner("owner")
                .researchArea("researchArea")
                .description("description")
                .researchQuestions(List.of("research question"))
                .members(List.of("test test"))
                .totalStudiesImported(2)
                .removedDuplicates(1)
                .selectedStudies(1)
                .build();

        var resource = reportFactory.createReport(reportData);

        assertNotNull(resource);
    }
}
