package com.dkwasniak.slr_spot_backend.deduplication;

import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.study.StudyService;
import com.dkwasniak.slr_spot_backend.study.dto.IdentificationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DeduplicationServiceTest {

    @InjectMocks
    private DeduplicationService deduplicationService;

    @Mock
    private StudyService studyService;

    @Test
    public void removeDuplicates_shouldRemoveDuplicatesByDoi_whenDeduplicationTypeDoi() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};

        when(studyService.getStudiesDoiByReviewId(1L)).thenReturn(new HashSet<>(){{
            add(study1.getDoi());
        }});
        var deduplicationDto = deduplicationService.removeDuplicates(1L, studies, DeduplicationType.DOI);

        assertEquals(1, deduplicationDto.getCorrectStudies().size());
    }

    @Test
    public void removeDuplicates_shouldRemoveDuplicatesByBasics_whenDeduplicationTypeBasicInformation() {
        var study1 = Study.builder().title("test1").authors("testAuthors1").publicationYear(2012).build();
        var study2 = Study.builder().title("test3").authors("testAuthors2").publicationYear(2012).build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};

        when(studyService.getStudyBasicInfoByReviewId(1L)).thenReturn(new HashSet<>(){{
            add(new IdentificationDto(study1.getTitle(), study1.getAuthors(), study1.getPublicationYear()));
        }});
        var deduplicationDto = deduplicationService.removeDuplicates(1L, studies, DeduplicationType.BASIC_INFORMATION);

        assertEquals(1, deduplicationDto.getCorrectStudies().size());
    }
}
