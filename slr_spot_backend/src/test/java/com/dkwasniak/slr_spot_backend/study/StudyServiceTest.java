package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
import com.dkwasniak.slr_spot_backend.study.dto.StatusDto;
import com.dkwasniak.slr_spot_backend.study.exception.StudyNotFoundException;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StudyServiceTest {

    @InjectMocks
    private StudyService studyService;

    @Mock
    private StudyRepository studyRepository;

    @Test
    public void getStudyById_shouldReturnStudy_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();

        when(studyRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.ofNullable(study1));
        var studyById = studyService.getStudyById(1L);

        assertEquals("test1", studyById.getTitle());
    }

    @Test
    public void getStudyById_shouldThrowStudyNotFoundException_whenNotExists() {
        when(studyRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.empty());

        assertThrows(StudyNotFoundException.class,
                () -> studyService.getStudyById(1L));
    }

    @Test
    public void getStudiesByReviewId_shouldReturnReviewStudies_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};
        Pageable pageRq = PageRequest.of(0, 10);

        when(studyRepository.findAllByStudyImport_Review_Id(1L, pageRq))
                .thenReturn(new PageImpl<>(studies));
        var studiesByReviewId = studyService.getStudiesByReviewId(
                1L, pageRq
        );

        assertEquals(2, studiesByReviewId.getContent().size());
    }

    @Test
    public void removeStudyById() {
        doNothing().when(studyRepository).deleteById(anyLong());

        studyService.removeStudyById(1L);

        verify(studyRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void getStudiesToBeReviewed_shouldReturnReviewStudiesToBeReviewed_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};
        Pageable pageRq = PageRequest.of(0, 10);

        when(studyRepository.findAllToBeReviewed(1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, pageRq))
                .thenReturn(new PageImpl<>(studies));
        var studiesByReviewId = studyService.getStudiesToBeReviewed(
                1L, 1L, Stage.TITLE_ABSTRACT, pageRq
        );

        assertEquals(2, studiesByReviewId.getContent().size());
    }

    @Test
    public void getStudiesAwaiting_shouldReturnReviewStudiesAwaiting_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};
        Pageable pageRq = PageRequest.of(0, 10);

        when(studyRepository.findAllAwaiting(1L, 1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, pageRq))
                .thenReturn(new PageImpl<>(studies));
        var studiesByReviewId = studyService.getStudiesAwaiting(
                1L, 1L, Stage.TITLE_ABSTRACT, pageRq
        );

        assertEquals(2, studiesByReviewId.getContent().size());
    }

    @Test
    public void getStudiesListByState_shouldReturnReviewStudiesByState_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};
        Pageable pageRq = PageRequest.of(0, 10);

        when(studyRepository.findAllByStudyImport_Review_IdAndState(anyLong(), any()))
                .thenReturn(studies);
        var studiesByReviewId = studyService.getStudiesListByState(
                1L, StudyState.INCLUDED
        );

        assertEquals(2, studiesByReviewId.size());
    }

    @Test
    public void getStudiesListByStage_shouldReturnReviewStudiesByStage_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};
        Pageable pageRq = PageRequest.of(0, 10);

        when(studyRepository.findAllByStudyImport_Review_IdAndStage(anyLong(), any()))
                .thenReturn(studies);
        var studiesByReviewId = studyService.getStudiesListByStage(
                1L, Stage.TITLE_ABSTRACT
        );

        assertEquals(2, studiesByReviewId.size());
    }

    @Test
    public void getStudiesByStageAndState_shouldReturnReviewStudiesByStageAndState_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};
        Pageable pageRq = PageRequest.of(0, 10);

        when(studyRepository.findAllByStudyImport_Review_IdAndStageAndState(anyLong(), any(), any(), any()))
                .thenReturn(new PageImpl<>(studies));
        var studiesByReviewId = studyService.getStudiesByStageAndState(
                1L, Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED, pageRq
        );

        assertEquals(2, studiesByReviewId.getContent().size());
    }

    @Test
    public void getStudyTags_shouldReturnStudyTags_whenAssigned() {
        var study1 = Study.builder().title("test1").build();
        Set<Tag> tags = new HashSet<>() {{
            add(new Tag());
            add(new Tag());
        }};
        study1.setTags(tags);

        var tagSet = studyService.getStudyTags(study1);

        assertEquals(2, tagSet.size());
    }

    @Test
    public void addTagToStudy_shouldAddStudyTag() {
        var study1 = Study.builder().title("test1").build();
        Set<Tag> tags = new HashSet<>() {{
            add(new Tag());
            add(new Tag());
        }};
        study1.setTags(tags);
        var newTag = new Tag();
        newTag.setStudies(new ArrayList<>());

        when(studyRepository.save(any())).thenReturn(study1);
        studyService.addTagToStudy(study1, newTag);

        verify(studyRepository, times(1)).save(any());
    }

    @Test
    public void removeTagFromStudy_shouldRemoveTag() {
        var study1 = Study.builder().title("test1").build();
        var oldTag = new Tag();
        oldTag.setStudies(new ArrayList<>(){{ add(study1); }});
        Set<Tag> tags = new HashSet<>() {{
            add(oldTag);
            add(new Tag());
        }};
        study1.setTags(tags);

        when(studyRepository.save(any())).thenReturn(study1);
        studyService.removeTagFromStudy(study1, oldTag);

        assertEquals(1, study1.getTags().size());
        verify(studyRepository, times(1)).save(any());
    }

    @Test
    public void getStudyComments_shouldReturnStudyComments_whenExists() {
        var study1 = Study.builder().title("test1").build();
        List<Comment> comments = new ArrayList<>() {{
            add(new Comment());
            add(new Comment());
        }};
        study1.setComments(comments);

        var commentList = studyService.getStudyComments(study1);

        assertEquals(2, commentList.size());
    }

    @Test
    public void addCommentToStudy_shouldAddStudyTag() {
        var study1 = Study.builder().title("test1").build();
        List<Comment> comments = new ArrayList<>() {{
            add(new Comment());
            add(new Comment());
        }};
        study1.setComments(comments);
        var newComment = new Comment();

        when(studyRepository.save(any())).thenReturn(study1);
        studyService.addCommentToStudy(study1, newComment);

        assertEquals(3, study1.getComments().size());
        verify(studyRepository, times(1)).save(any());
    }

    @Test
    public void isStudyScreeningAllowed_shouldCheckIfScreeningAllowed() {
        var study1 = Study.builder().title("test1").build();
        study1.setStage(Stage.TITLE_ABSTRACT);
        study1.setState(StudyState.TO_BE_REVIEWED);

        assertTrue(studyService.isStudyScreeningAllowed(study1));

        study1.setStage(Stage.FULL_TEXT);

        assertTrue(studyService.isStudyScreeningAllowed(study1));
    }

    @Test
    public void addScreeningDecisionToStudy() {
        var study1 = Study.builder().title("test1").build();
        study1.setScreeningDecisions(new ArrayList<>());
        var user = new User("test", "test", "test@gmail.com", "123");
        user.setScreeningDecisions(new HashSet<>());
        var decision = new ScreeningDecision(Decision.INCLUDE, Stage.TITLE_ABSTRACT);

        when(studyRepository.save(any())).thenReturn(study1);
        studyService.addScreeningDecisionToStudy(user, study1, decision);

        assertEquals(Decision.INCLUDE, study1.getScreeningDecisions().get(0).getDecision());
        verify(studyRepository, times(1)).save(any());
    }

    @Test
    public void verifyStudyStatus_shouldReturnNewStatusDto_whenTitleAbstract() {
        var study1 = Study.builder().title("test1").build();
        study1.setStage(Stage.TITLE_ABSTRACT);
        study1.setState(StudyState.TO_BE_REVIEWED);
        List<ScreeningDecision> screeningDecisions = new ArrayList<>();
        screeningDecisions.add(new ScreeningDecision(Decision.INCLUDE, Stage.TITLE_ABSTRACT));
        screeningDecisions.add(new ScreeningDecision(Decision.INCLUDE, Stage.TITLE_ABSTRACT));
        study1.setScreeningDecisions(screeningDecisions);

        assertEquals(Stage.FULL_TEXT, studyService.verifyStudyStatus(study1, 2).getStage());
        assertEquals(StudyState.TO_BE_REVIEWED, studyService.verifyStudyStatus(study1, 2).getState());

        screeningDecisions = new ArrayList<>();
        screeningDecisions.add(new ScreeningDecision(Decision.EXCLUDE, Stage.TITLE_ABSTRACT));
        screeningDecisions.add(new ScreeningDecision(Decision.EXCLUDE, Stage.TITLE_ABSTRACT));
        study1.setScreeningDecisions(screeningDecisions);

        assertEquals(Stage.TITLE_ABSTRACT, studyService.verifyStudyStatus(study1, 2).getStage());
        assertEquals(StudyState.EXCLUDED, studyService.verifyStudyStatus(study1, 2).getState());

        screeningDecisions = new ArrayList<>();
        screeningDecisions.add(new ScreeningDecision(Decision.EXCLUDE, Stage.TITLE_ABSTRACT));
        screeningDecisions.add(new ScreeningDecision(Decision.INCLUDE, Stage.TITLE_ABSTRACT));
        study1.setScreeningDecisions(screeningDecisions);

        assertEquals(Stage.TITLE_ABSTRACT, studyService.verifyStudyStatus(study1, 2).getStage());
        assertEquals(StudyState.CONFLICTED, studyService.verifyStudyStatus(study1, 2).getState());

        screeningDecisions = new ArrayList<>();
        screeningDecisions.add(new ScreeningDecision(Decision.EXCLUDE, Stage.TITLE_ABSTRACT));
        study1.setScreeningDecisions(screeningDecisions);

        assertEquals(Stage.TITLE_ABSTRACT, studyService.verifyStudyStatus(study1, 2).getStage());
        assertEquals(StudyState.TO_BE_REVIEWED, studyService.verifyStudyStatus(study1, 2).getState());
    }

    @Test
    public void verifyStudyStatus_shouldReturnNewStatusDto_whenFullText() {
        var study1 = Study.builder().title("test1").build();
        study1.setStage(Stage.FULL_TEXT);
        study1.setState(StudyState.TO_BE_REVIEWED);
        List<ScreeningDecision> screeningDecisions = new ArrayList<>();
        screeningDecisions.add(new ScreeningDecision(Decision.INCLUDE, Stage.FULL_TEXT));
        screeningDecisions.add(new ScreeningDecision(Decision.INCLUDE, Stage.FULL_TEXT));
        study1.setScreeningDecisions(screeningDecisions);

        assertEquals(Stage.FULL_TEXT, studyService.verifyStudyStatus(study1, 2).getStage());
        assertEquals(StudyState.INCLUDED, studyService.verifyStudyStatus(study1, 2).getState());

        screeningDecisions = new ArrayList<>();
        screeningDecisions.add(new ScreeningDecision(Decision.EXCLUDE, Stage.FULL_TEXT));
        screeningDecisions.add(new ScreeningDecision(Decision.EXCLUDE, Stage.FULL_TEXT));
        study1.setScreeningDecisions(screeningDecisions);

        assertEquals(Stage.FULL_TEXT, studyService.verifyStudyStatus(study1, 2).getStage());
        assertEquals(StudyState.EXCLUDED, studyService.verifyStudyStatus(study1, 2).getState());

        screeningDecisions = new ArrayList<>();
        screeningDecisions.add(new ScreeningDecision(Decision.EXCLUDE, Stage.FULL_TEXT));
        screeningDecisions.add(new ScreeningDecision(Decision.INCLUDE, Stage.FULL_TEXT));
        study1.setScreeningDecisions(screeningDecisions);

        assertEquals(Stage.FULL_TEXT, studyService.verifyStudyStatus(study1, 2).getStage());
        assertEquals(StudyState.CONFLICTED, studyService.verifyStudyStatus(study1, 2).getState());

        screeningDecisions = new ArrayList<>();
        screeningDecisions.add(new ScreeningDecision(Decision.EXCLUDE, Stage.FULL_TEXT));
        study1.setScreeningDecisions(screeningDecisions);

        assertEquals(Stage.FULL_TEXT, studyService.verifyStudyStatus(study1, 2).getStage());
        assertEquals(StudyState.TO_BE_REVIEWED, studyService.verifyStudyStatus(study1, 2).getState());
    }

    @Test
    public void updateStudyStatus_shouldUpdateStatus_whenDifferent() {
        var study1 = Study.builder().title("test1").build();
        study1.setStage(Stage.TITLE_ABSTRACT);
        study1.setState(StudyState.TO_BE_REVIEWED);

        when(studyRepository.save(any())).thenReturn(study1);
        studyService.updateStudyStatus(study1, StatusDto.of(Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED));

        assertEquals(Stage.TITLE_ABSTRACT, study1.getStage());
        assertEquals(StudyState.TO_BE_REVIEWED, study1.getState());

        studyService.updateStudyStatus(study1, StatusDto.of(Stage.FULL_TEXT, StudyState.INCLUDED));

        assertEquals(Stage.FULL_TEXT, study1.getStage());
        assertEquals(StudyState.INCLUDED, study1.getState());
    }

    @Test
    public void clearDecisions_shouldClearScreeningDecisions() {
        var study1 = Study.builder().title("test1").build();
        study1.setStage(Stage.TITLE_ABSTRACT);
        study1.setState(StudyState.TO_BE_REVIEWED);
        List<ScreeningDecision> screeningDecisions = new ArrayList<>();
        screeningDecisions.add(new ScreeningDecision(Decision.INCLUDE, Stage.TITLE_ABSTRACT));
        screeningDecisions.add(new ScreeningDecision(Decision.INCLUDE, Stage.TITLE_ABSTRACT));
        study1.setScreeningDecisions(screeningDecisions);

        when(studyRepository.save(any())).thenReturn(study1);
        studyService.clearDecisions(study1);

        assertEquals(0, study1.getScreeningDecisions().size());

        study1.setStage(Stage.FULL_TEXT);
        screeningDecisions.add(new ScreeningDecision(Decision.INCLUDE, Stage.FULL_TEXT));
        screeningDecisions.add(new ScreeningDecision(Decision.INCLUDE, Stage.FULL_TEXT));
        study1.setScreeningDecisions(screeningDecisions);

        when(studyRepository.save(any())).thenReturn(study1);
        studyService.clearDecisions(study1);

        assertEquals(0, study1.getScreeningDecisions().size());
    }
}
