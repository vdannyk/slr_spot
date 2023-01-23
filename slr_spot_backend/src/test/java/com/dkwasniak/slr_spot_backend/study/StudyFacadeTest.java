package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.comment.CommentService;
import com.dkwasniak.slr_spot_backend.comment.dto.CommentDto;
import com.dkwasniak.slr_spot_backend.comment.dto.CommentRequest;
import com.dkwasniak.slr_spot_backend.file.FileService;
import com.dkwasniak.slr_spot_backend.file.exception.NotAllowedFileContentTypeException;
import com.dkwasniak.slr_spot_backend.operation.Operation;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningService;
import com.dkwasniak.slr_spot_backend.screeningDecision.dto.ScreeningDecisionDto;
import com.dkwasniak.slr_spot_backend.study.document.Document;
import com.dkwasniak.slr_spot_backend.study.dto.StatusDto;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.tag.TagService;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StudyFacadeTest {

    @InjectMocks
    private StudyFacade studyFacade;

    @Mock
    private StudyService studyService;
    @Mock
    private TagService tagService;
    @Mock
    private CommentService commentService;
    @Mock
    private UserService userService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private ScreeningService screeningService;
    @Mock
    private FileService fileService;

    @Test
    public void getStudyById_shouldReturnStudy_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();

        when(studyService.getStudyById(anyLong()))
                .thenReturn(study1);
        var studyById = studyFacade.getStudyById(1L);

        assertEquals("test1", studyById.getTitle());
    }

    @Test
    public void getStudiesByReviewId_shouldReturnReviewStudies_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};

        when(studyService.getStudiesByReviewId(any(), any()))
                .thenReturn(new PageImpl<>(studies));
        var studiesByReviewId = studyFacade.getStudiesByReviewId(1L, 0, 10, SortProperty.TITLE, Sort.Direction.ASC);

        assertEquals(2, studiesByReviewId.getContent().size());
    }

    @Test
    public void removeStudyById() {
        doNothing().when(studyService).removeStudyById(anyLong());

        studyFacade.removeStudyById(1L);

        verify(studyService, times(1)).removeStudyById(anyLong());
    }

    @Test
    public void getStudiesToBeReviewed_shouldReturnReviewStudiesToBeReviewed_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};

        when(studyService.getStudiesToBeReviewed(anyLong(), anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(studies));
        var studiesByReviewId = studyFacade.getStudiesToBeReviewed(
                1L, 1L, Stage.TITLE_ABSTRACT, 0, 10, SortProperty.TITLE, Sort.Direction.ASC
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

        when(studyService.getStudiesAwaiting(anyLong(), anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(studies));
        var studiesByReviewId = studyFacade.getStudiesAwaiting(
                1L, 1L, Stage.TITLE_ABSTRACT, 0, 10, SortProperty.TITLE, Sort.Direction.ASC
        );

        assertEquals(2, studiesByReviewId.getContent().size());
    }

    @Test
    public void getStudiesConflicted_shouldReturnReviewStudiesConflicted_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};

        when(studyService.getStudiesByStageAndState(anyLong(), any(), any(), any()))
                .thenReturn(new PageImpl<>(studies));
        var studiesByReviewId = studyFacade.getStudiesConflicted(
                1L, Stage.TITLE_ABSTRACT, 0, 10, SortProperty.TITLE, Sort.Direction.ASC
        );

        assertEquals(2, studiesByReviewId.getContent().size());
    }

    @Test
    public void getStudiesExcluded_shouldReturnReviewStudiesExcluded_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};

        when(studyService.getStudiesByStageAndState(anyLong(), any(), any(), any()))
                .thenReturn(new PageImpl<>(studies));
        var studiesByReviewId = studyFacade.getStudiesExcluded(
                1L, Stage.TITLE_ABSTRACT, 0, 10, SortProperty.TITLE, Sort.Direction.ASC
        );

        assertEquals(2, studiesByReviewId.getContent().size());
    }

    @Test
    public void getDuplicates_shouldReturnReviewStudiesDuplicates_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};

        when(studyService.getStudiesByState(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(studies));
        var studiesByReviewId = studyFacade.getDuplicates(
                1L, 0, 10
        );

        assertEquals(2, studiesByReviewId.getContent().size());
    }

    @Test
    public void getIncludedStudies_shouldReturnReviewStudiesIncluded_whenExists() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};

        when(studyService.getStudiesByState(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(studies));
        var studiesByReviewId = studyFacade.getIncludedStudies(
                1L, 0, 10, SortProperty.TITLE, Sort.Direction.ASC
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

        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        when(studyService.getStudyTags(any())).thenReturn(study1.getTags());
        var tagSet = studyFacade.getStudyTags(1L);

        assertEquals(2, tagSet.size());
    }

    @Test
    public void addTagToStudy_shouldAddStudyTag() {
        var study1 = Study.builder().title("test1").build();
        study1.setOperations(new ArrayList<>());
        var newTag = new Tag("test");
        newTag.setStudies(new ArrayList<>());

        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        when(tagService.getTagById(anyLong())).thenReturn(newTag);
        doNothing().when(studyService).addTagToStudy(any(), any());
        studyFacade.addTagToStudy(1L, 1L);

        verify(studyService, times(1)).addTagToStudy(any(), any());
    }

    @Test
    public void removeTagFromStudy_shouldCallRemoveTag() {
        var study1 = Study.builder().title("test1").build();
        study1.setOperations(new ArrayList<>());
        var newTag = new Tag("test");
        newTag.setStudies(new ArrayList<>());

        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        when(tagService.getTagById(anyLong())).thenReturn(newTag);
        doNothing().when(studyService).removeTagFromStudy(any(), any());
        studyFacade.removeTagFromStudy(1L, 1L);

        verify(studyService, times(1)).removeTagFromStudy(any(), any());
    }

    @Test
    public void getStudyComments_shouldReturnStudyComments_whenExists() {
        var study1 = Study.builder().title("test1").build();
        List<Comment> comments = new ArrayList<>() {{
            add(new Comment("content1"));
            add(new Comment("content2"));
        }};
        study1.setComments(comments);

        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        when(studyService.getStudyComments(any())).thenReturn(study1.getComments());
        when(commentService.toCommentDto(any())).thenReturn(new CommentDto()).thenReturn(new CommentDto());
        var commentList = studyFacade.getStudyComments(1L);

        assertEquals(2, commentList.size());
    }

    @Test
    public void addCommentToStudy_shouldAddStudyComment() {
        var study1 = Study.builder().title("test1").build();
        var user = new User("test", "test", "test@gmail.com", "123");
        var commentRq = new CommentRequest(1L, "content");

        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        when(userService.getUserById(anyLong())).thenReturn(user);
        doNothing().when(studyService).addCommentToStudy(any(), any());
        doNothing().when(studyService).addOperation(any(), any());
        studyFacade.addCommentToStudy(1L, commentRq);

        verify(studyService, times(1)).addCommentToStudy(any(), any());
    }

    @Test
    public void addStudyScreeningDecision_shouldUpdateScreeningDecision_whenExist() {
        var study1 = Study.builder().id(1L).title("test1").build();
        var user = new User("test", "test", "test@gmail.com", "123");
        var review = new Review();
        review.setScreeningReviewers(1);
        var screeningDecisionDto = new ScreeningDecisionDto(1L, 1L, Decision.INCLUDE, Stage.TITLE_ABSTRACT);
        List<ScreeningDecision> screeningDecisions = new ArrayList<>();
        var sd = new ScreeningDecision(Decision.INCLUDE, Stage.TITLE_ABSTRACT);
        sd.setStudy(study1);
        screeningDecisions.add(sd);
        user.setScreeningDecisions(new HashSet<>(screeningDecisions));
        var statusDto = StatusDto.of(Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED);

        when(reviewService.getReviewById(anyLong())).thenReturn(review);
        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        when(studyService.isStudyScreeningAllowed(any())).thenReturn(true);
        when(userService.getUserById(anyLong())).thenReturn(user);
        doNothing().when(screeningService).updateDecision(any(), any());
        when(studyService.verifyStudyStatus(any(), anyInt())).thenReturn(statusDto);
        studyFacade.addStudyScreeningDecision(1L, screeningDecisionDto);

        verify(screeningService, times(1)).updateDecision(any(), any());
    }

    @Test
    public void addStudyScreeningDecision_shouldAddScreeningDecision_whenNotExist() {
        var study1 = Study.builder().id(1L).title("test1").build();
        var user = new User("test", "test", "test@gmail.com", "123");
        var review = new Review();
        review.setScreeningReviewers(1);
        var screeningDecisionDto = new ScreeningDecisionDto(1L, 1L, Decision.INCLUDE, Stage.TITLE_ABSTRACT);
        var statusDto = StatusDto.of(Stage.TITLE_ABSTRACT, StudyState.TO_BE_REVIEWED);

        when(reviewService.getReviewById(anyLong())).thenReturn(review);
        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        when(studyService.isStudyScreeningAllowed(any())).thenReturn(true);
        when(userService.getUserById(anyLong())).thenReturn(user);
        doNothing().when(studyService).addScreeningDecisionToStudy(any(), any(), any());
        when(studyService.verifyStudyStatus(any(), anyInt())).thenReturn(statusDto);
        studyFacade.addStudyScreeningDecision(1L, screeningDecisionDto);

        verify(studyService, times(1)).addScreeningDecisionToStudy(any(), any(), any());
    }

    @Test
    public void getFullTextDocument_shouldReturnStudyDocument() {
        var study1 = Study.builder().id(1L).title("test1").build();
        study1.setFullText(new Document());

        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        var doc = studyFacade.getFullTextDocument(1L);

        assertNotNull(doc);
    }

    @Test
    public void getFullTextDocumentName_shouldReturnStudyDocumentName() {
        var study1 = Study.builder().id(1L).title("test1").build();
        var document = new Document();
        document.setName("test");
        study1.setFullText(document);

        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        var doc = studyFacade.getFullTextDocumentName(1L);

        assertEquals("test", doc);
    }

    @Test
    public void addFullTextDocument_shouldReturnStudyDocumentName() {
        var study1 = Study.builder().id(1L).title("test1").build();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", "test", "test", new byte[0]);

        when(fileService.isPdfFile(anyString())).thenReturn(true);
        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        when(studyService.updateStudy(any())).thenReturn(study1);
        var doc = studyFacade.addFullTextDocument(1L, mockMultipartFile);

        assertEquals("test", doc.getName());
    }

    @Test
    public void addFullTextDocument_shouldThrowException_whenContentTypeNotAllowed() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", "test", "test", new byte[0]);
        when(fileService.isPdfFile(anyString())).thenReturn(false);

        assertThrows(NotAllowedFileContentTypeException.class,
                () -> studyFacade.addFullTextDocument(any(), mockMultipartFile));
    }

    @Test
    public void deleteFullTextDocument_shouldRemoveStudyDocumentName() {
        var study1 = Study.builder().id(1L).title("test1").build();
        var document = new Document();
        document.setName("test");
        study1.setFullText(document);

        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        when(studyService.updateStudy(any())).thenReturn(study1);
        studyFacade.deleteFullTextDocument(1L);

        assertNull(study1.getFullText());
    }

    @Test
    public void getStudiesCountByStatus_shouldReturnStudiesCount() {
        when(studyService.getStudiesCountByStage(anyLong(), any())).thenReturn(2);
        int count = studyFacade.getStudiesCountByStatus(1L, Stage.TITLE_ABSTRACT.name());
        assertEquals(2, count);

        when(studyService.getStudiesCountByState(anyLong(), any())).thenReturn(1);
        count = studyFacade.getStudiesCountByStatus(1L, StudyState.TO_BE_REVIEWED.name());
        assertEquals(1, count);

        assertThrows(IllegalStateException.class,
                () -> studyFacade.getStudiesCountByStatus(1L, "TEST"));
    }

    @Test
    public void exportStudiesByStatus() {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test3").doi("456").build();
        List<Study> studies = new ArrayList<>() {{
            add(study1);
            add(study2);
        }};

        doNothing().when(fileService).checkIfExportFileFormatAllowed(anyString());
        when(studyService.getStudiesListByStage(anyLong(), any())).thenReturn(studies);
        when(fileService.write(any(), anyString())).thenReturn(new InputStreamResource(new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray())));

        var resource = studyFacade.exportStudiesByStatus(1L, Stage.TITLE_ABSTRACT.name(), "CSV");

        assertNotNull(resource);

        doNothing().when(fileService).checkIfExportFileFormatAllowed(anyString());
        when(studyService.getStudiesListByState(anyLong(), any())).thenReturn(studies);
        when(fileService.write(any(), anyString())).thenReturn(new InputStreamResource(new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray())));

        resource = studyFacade.exportStudiesByStatus(1L, StudyState.TO_BE_REVIEWED.name(), "CSV");

        assertNotNull(resource);

        assertThrows(IllegalStateException.class,
                () -> studyFacade.exportStudiesByStatus(1L, "test", "CSV"));
    }

    @Test
    public void getStudyHistory_shouldReturnOperations_whenExists() {
        var study1 = Study.builder().id(1L).title("test1").build();
        List<Operation> operations = new ArrayList<>();
        operations.add(new Operation("desc1"));
        operations.add(new Operation("desc2"));
        study1.setOperations(operations);

        when(studyService.getStudyById(anyLong())).thenReturn(study1);
        when(studyService.getStudyHistory(any())).thenReturn(operations);
        var history = studyFacade.getStudyHistory(1L);

        assertEquals(2, history.size());
    }
}
