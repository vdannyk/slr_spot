package com.dkwasniak.slr_spot_backend.study;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class SearchProcessorTest {

    @InjectMocks
    private SearchProcessor searchProcessor;

    @Mock
    private StudyRepository studyRepository;

    @Test
    public void searchAll() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test2").doi("1234").build();
        List<Study> studies = List.of(study1, study2);
        Pageable pageRq = PageRequest.of(0, 10);

        when(studyRepository.findByStudyImport_Review_IdAndTitleContaining(anyLong(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findByStudyImport_Review_IdAndAuthorsContaining(anyLong(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findByStudyImport_Review_IdAndPublicationYearContaining(anyLong(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findByStudyImport_Review_IdAndTitleContainingOrAuthorsContaining(anyLong(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findByStudyImport_Review_IdAndTitleContainingPublicationYearContaining(anyLong(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findByStudyImport_Review_IdAndAuthorsContainingPublicationYearContaining(anyLong(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findByStudyImport_Review_IdAndTitleContainingAuthorsContainingPublicationYearContaining(anyLong(), anyString(), any())).thenReturn(new PageImpl<>(studies));

        assertEquals(2, searchProcessor.searchAll(StudySearchType.TITLE, 1L, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAll(StudySearchType.AUTHORS, 1L, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAll(StudySearchType.YEAR, 1L, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAll(StudySearchType.TITLE_AUTHORS, 1L, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAll(StudySearchType.AUTHORS_YEAR, 1L, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAll(StudySearchType.TITLE_AUTHORS_YEAR, 1L, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAll(StudySearchType.TITLE_YEAR, 1L, "testSearch", pageRq).getContent().size());
    }

    @Test
    public void searchConflicted() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test2").doi("1234").build();
        List<Study> studies = List.of(study1, study2);
        Pageable pageRq = PageRequest.of(0, 10);

        when(studyRepository.findConflictedByTitleContaining(anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findConflictedByAuthorsContaining(anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findConflictedByPublicationYearContaining(anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findConflictedByTitleAndAuthors(anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findConflictedByTitleAndYear(anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findConflictedByAuthorsAndYear(anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findConflictedByTitleAndAuthorsAndYear(anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));

        assertEquals(2, searchProcessor.searchConflicted(StudySearchType.TITLE, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchConflicted(StudySearchType.AUTHORS, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchConflicted(StudySearchType.YEAR, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchConflicted(StudySearchType.TITLE_AUTHORS, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchConflicted(StudySearchType.AUTHORS_YEAR, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchConflicted(StudySearchType.TITLE_AUTHORS_YEAR, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchConflicted(StudySearchType.TITLE_YEAR, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
    }

    @Test
    public void searchToBeReviewed() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test2").doi("1234").build();
        List<Study> studies = List.of(study1, study2);
        Pageable pageRq = PageRequest.of(0, 10);

        when(studyRepository.findToBeReviewedByTitleContaining(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findToBeReviewedByAuthorsContaining(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findToBeReviewedByPublicationYearContaining(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findToBeReviewedByTitleAndAuthors(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findToBeReviewedByTitleAndYear(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findToBeReviewedByAuthorsAndYear(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findToBeReviewedByTitleAndAuthorsAndYear(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));

        assertEquals(2, searchProcessor.searchToBeReviewed(StudySearchType.TITLE, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchToBeReviewed(StudySearchType.AUTHORS, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchToBeReviewed(StudySearchType.YEAR, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchToBeReviewed(StudySearchType.TITLE_AUTHORS, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchToBeReviewed(StudySearchType.AUTHORS_YEAR, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchToBeReviewed(StudySearchType.TITLE_AUTHORS_YEAR, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchToBeReviewed(StudySearchType.TITLE_YEAR, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
    }

    @Test
    public void searchAwaiting() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test2").doi("1234").build();
        List<Study> studies = List.of(study1, study2);
        Pageable pageRq = PageRequest.of(0, 10);

        when(studyRepository.findAwaitingByTitleContaining(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findAwaitingByAuthorsContaining(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findAwaitingByPublicationYearContaining(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findAwaitingByTitleAndAuthors(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findAwaitingByTitleAndYear(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findAwaitingByAuthorsAndYear(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));
        when(studyRepository.findAwaitingByTitleAndAuthorsAndYear(anyLong(), anyLong(), any(), any(), anyString(), any())).thenReturn(new PageImpl<>(studies));

        assertEquals(2, searchProcessor.searchAwaiting(StudySearchType.TITLE, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAwaiting(StudySearchType.AUTHORS, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAwaiting(StudySearchType.YEAR, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAwaiting(StudySearchType.TITLE_AUTHORS, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAwaiting(StudySearchType.AUTHORS_YEAR, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAwaiting(StudySearchType.TITLE_AUTHORS_YEAR, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
        assertEquals(2, searchProcessor.searchAwaiting(StudySearchType.TITLE_YEAR, 1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, "testSearch", pageRq).getContent().size());
    }
}
