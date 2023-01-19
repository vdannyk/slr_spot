package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.comment.CommentService;
import com.dkwasniak.slr_spot_backend.comment.dto.CommentDto;
import com.dkwasniak.slr_spot_backend.comment.dto.CommentRequest;
import com.dkwasniak.slr_spot_backend.study.document.Document;
import com.dkwasniak.slr_spot_backend.file.FileService;
import com.dkwasniak.slr_spot_backend.file.exception.NotAllowedFileContentTypeException;
import com.dkwasniak.slr_spot_backend.operation.Operation;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningService;
import com.dkwasniak.slr_spot_backend.screeningDecision.dto.ScreeningDecisionDto;
import com.dkwasniak.slr_spot_backend.study.dto.StatusDto;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.tag.TagService;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;


@Component
@RequiredArgsConstructor
public class StudyFacade {

    private final StudyService studyService;
    private final FileService fileService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final TagService tagService;
    private final CommentService commentService;
    private final ScreeningService screeningService;
    private final SearchProcessor searchProcessor;

    public Study getStudyById(Long studyId) {
        return studyService.getStudyById(studyId);
    }

    public Page<Study> getStudiesByReviewId(Long reviewId, int page, int size,
                                            SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return studyService.getStudiesByReviewId(reviewId, pageRq);
    }

    public void removeStudyById(Long studyId) {
        studyService.removeStudyById(studyId);
    }

    public Page<Study> getStudiesToBeReviewed(Long reviewId, Long userId, Stage stage, int page, int size,
                                              SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return studyService.getStudiesToBeReviewed(reviewId, userId, stage, pageRq);
    }

    public Page<Study> getStudiesAwaiting(Long reviewId, Long userId, Stage stage, int page, int size,
                                          SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return studyService.getStudiesAwaiting(reviewId, userId, stage, pageRq);
    }

    public Page<Study> getStudiesConflicted(Long reviewId, Stage stage, int page, int size,
                                            SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return studyService.getStudiesByStageAndState(reviewId, stage, StudyState.CONFLICTED, pageRq);
    }

    public Page<Study> getStudiesExcluded(Long reviewId, Stage stage, int page, int size,
                                          SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return studyService.getStudiesByStageAndState(reviewId, stage, StudyState.EXCLUDED, pageRq);
    }

    public Page<Study> getDuplicates(Long reviewId, int page, int size) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));
        return studyService.getStudiesByState(reviewId, StudyState.DUPLICATES, pageRq);
    }

    public Page<Study> getIncludedStudies(Long reviewId, int page, int size,
                                          SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return studyService.getStudiesByState(reviewId, StudyState.INCLUDED, pageRq);
    }

    public Set<Tag> getStudyTags(Long studyId) {
        Study study = studyService.getStudyById(studyId);
        return studyService.getStudyTags(study);
    }

    public void addTagToStudy(Long studyId, Long tagId) {
        Study study = studyService.getStudyById(studyId);
        Tag tag = tagService.getTagById(tagId);
        Operation operation = new Operation(String.format(OperationDescription.ADD_TAG.getDescription(), tag.getName()));
        study.addOperation(operation);
        studyService.addTagToStudy(study, tag);
    }

    public void removeTagFromStudy(Long studyId, Long tagId) {
        Study study = studyService.getStudyById(studyId);
        Tag tag = tagService.getTagById(tagId);
        Operation operation = new Operation(String.format(OperationDescription.REMOVE_TAG.getDescription(), tag.getName()));
        study.addOperation(operation);
        studyService.removeTagFromStudy(study, tag);
    }

    public List<CommentDto> getStudyComments(Long studyId) {
        Study study = studyService.getStudyById(studyId);
        List<Comment> comments = studyService.getStudyComments(study);
        comments.sort(Comparator.comparing(Comment::getDate));
        return comments.stream().map(commentService::toCommentDto)
                .collect(Collectors.toList());
    }

    public void addCommentToStudy(Long studyId, CommentRequest commentRequest) {
        Study study = studyService.getStudyById(studyId);
        User user = userService.getUserById(commentRequest.getUserId());
        Comment comment = new Comment(commentRequest.getContent());
        comment.setUser(user);
        studyService.addCommentToStudy(study, comment);
        studyService.addOperation(study,
                new Operation(String.format(OperationDescription.ADD_COMMENT.getDescription(), user.getEmail())));
    }

    public void addStudyScreeningDecision(Long studyId, ScreeningDecisionDto screeningDecisionDto) {
        Review review = reviewService.getReviewById(screeningDecisionDto.getReviewId());
        int requiredReviewers = review.getScreeningReviewers();
        Study study = studyService.getStudyById(studyId);
        if (studyService.isStudyScreeningAllowed(study)) {
            User user = userService.getUserById(screeningDecisionDto.getUserId());

            Optional<ScreeningDecision> oldDecision = user.getScreeningDecisions()
                    .stream()
                    .filter(sd -> Objects.equals(sd.getStudy().getId(), studyId) && screeningDecisionDto.getStage().equals(sd.getStage())).findFirst();

            if (oldDecision.isPresent() && oldDecision.get().getStage().equals(screeningDecisionDto.getStage())) {
                studyService.addOperation(study,
                        new Operation(String.format(OperationDescription.CHANGE_VOTE.getDescription(), user.getEmail())));
                screeningService.updateDecision(oldDecision.get(), screeningDecisionDto.getDecision());
            } else {
                ScreeningDecision screeningDecision = new ScreeningDecision(
                        screeningDecisionDto.getDecision(), screeningDecisionDto.getStage()
                );
                studyService.addOperation(study,
                        new Operation(String.format(OperationDescription.VOTE.getDescription(), user.getEmail())));
                studyService.addScreeningDecisionToStudy(user, study, screeningDecision);
            }
            StatusDto newStatus = studyService.verifyStudyStatus(study, requiredReviewers);
            if (!newStatus.getStage().equals(study.getStage()) || !newStatus.getState().equals(study.getState())) {
                studyService.addOperation(study,
                        new Operation(String.format(OperationDescription.CHANGE_STATUS.getDescription(),
                                newStatus.getStage().name() + " " + newStatus.getState().name())));
                studyService.updateStudyStatus(study, newStatus);
            }
        }
    }

    public Decision getScreeningDecisionByUser(Long studyId, Long userId, Stage stage) {
        return screeningService.getScreeningDecisionByStudyIdAndUserId(studyId, userId, stage).getDecision();
    }

    @Transactional
    public void restoreStudy(Long studyId) {
        Study study = studyService.getStudyById(studyId);
        studyService.addOperation(study, new Operation(OperationDescription.RESTORE_TO_SCREENING.getDescription()));
        studyService.updateStudyStatus(study, StatusDto.of(study.getStage(), StudyState.TO_BE_REVIEWED));
        studyService.clearDecisions(study);
    }

    public void markStudyAsDuplicate(Long studyId) {
        Study study = studyService.getStudyById(studyId);
        studyService.addOperation(study, new Operation(OperationDescription.MARK_DUPLICATE.getDescription()));
        studyService.updateStudyStatus(study, StatusDto.of(study.getStage(), StudyState.DUPLICATES));
    }

    public void markStudiesAsDuplicate(List<Long> studiesId) {
        for (var id : studiesId) {
            markStudyAsDuplicate(id);
        }
    }

    public Document getFullTextDocument(Long studyId) {
        Study study = studyService.getStudyById(studyId);
        return study.getFullText();
    }

    public String getFullTextDocumentName(Long studyId) {
        Study study = studyService.getStudyById(studyId);
        Document document = study.getFullText();
        if (isNull(document)) {
            return null;
        }
        return document.getName();
    }


    public Document addFullTextDocument(Long studyId, MultipartFile file) {
        if (!fileService.isPdfFile(file.getContentType())) {
            throw new NotAllowedFileContentTypeException();
        }
        Study study = studyService.getStudyById(studyId);
        Document document = new Document();
        document.setName(file.getOriginalFilename());
        try {
            document.setData(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        study.setDocument(document);
        studyService.updateStudy(study);
        studyService.addOperation(study,
                new Operation(OperationDescription.LOAD_FULLTEXT.getDescription()));
        return document;
    }

    public void deleteFullTextDocument(Long studyId) {
        Study study = studyService.getStudyById(studyId);
        study.setFullText(null);
        studyService.addOperation(study,
                new Operation(OperationDescription.REMOVE_FULLTEXT.getDescription()));
        studyService.updateStudy(study);
    }

    public int getStudiesCountByStatus(Long reviewId, String status) {
        try {
            Stage stage = Stage.valueOf(status);
            return studyService.getStudiesCountByStage(reviewId, stage);
        } catch (Exception e) {
            try {
                StudyState state = StudyState.valueOf(status);
                return studyService.getStudiesCountByState(reviewId, state);
            } catch (Exception ex) {
                throw new IllegalStateException("Unsupported status type");
            }
        }
    }

    public InputStreamResource exportStudiesByStatus(Long reviewId, String status, String format) {
        fileService.checkIfExportFileFormatAllowed(format);
        List<Study> studiesToExport;
        try {
            Stage stage = Stage.valueOf(status);
            studiesToExport = studyService.getStudiesListByStage(reviewId, stage);
            return fileService.write(studiesToExport, format);
        } catch (Exception e) {
            try {
                StudyState state = StudyState.valueOf(status);
                studiesToExport = studyService.getStudiesListByState(reviewId, state);
                return fileService.write(studiesToExport, format);
            } catch (Exception ex) {
                throw new IllegalStateException("Unsupported status type");
            }
        }
    }

    public List<Operation> getStudyHistory(Long studyId) {
        Study study = studyService.getStudyById(studyId);
        List<Operation> history = studyService.getStudyHistory(study);
        history.sort(Comparator.comparing(Operation::getDate));
        return history;
    }

    public Page<Study> getStudiesByFolderId(Long folderId, Long reviewId, int page, int size) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));
        return studyService.getStudiesByFolderId(folderId, reviewId, pageRq);
    }

    public Page<Study> getStudiesToBeReviewedByFolderId(Long reviewId, Long folderId, Long userId,
                                                        Stage stage, int page, int size) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));
        return studyService.getStudiesToBeReviewedByFolderId(reviewId, folderId, userId, stage, pageRq);
    }

    public Page<Study> getStudiesConflictedByFolderId(Long reviewId, Long folderId, Stage stage,
                                                      int page, int size) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));
        return studyService.getStudiesByFolderIdAndStageAndState(reviewId, folderId, stage, StudyState.CONFLICTED, pageRq);
    }

    public Page<Study> getStudiesExcludedByFolderId(Long reviewId, Long folderId, Stage stage,
                                                      int page, int size) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));
        return studyService.getStudiesByFolderIdAndStageAndState(reviewId, folderId, stage, StudyState.EXCLUDED, pageRq);
    }

    public Page<Study> getStudiesAwaitingByFolderId(Long reviewId, Long folderId, Long userId,
                                                    Stage stage, int page, int size) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));
        return studyService.getStudiesAwaitingByFolderId(reviewId, folderId, userId, stage, pageRq);
    }


    public Page<Study> searchStudiesToBeReviewed(Long reviewId, Long userId, Stage stage, StudySearchType searchType,
                                                 String searchValue, int page, int size,
                                                 SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return searchProcessor.searchToBeReviewed(searchType, reviewId, userId, stage, StudyState.TO_BE_REVIEWED, searchValue, pageRq);
    }

    public Page<Study> searchStudiesConflicted(Long reviewId, Stage stage, StudySearchType searchType,
                                               String searchValue, int page, int size,
                                               SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return searchProcessor.searchConflicted(searchType, reviewId, stage, StudyState.CONFLICTED, searchValue, pageRq);
    }

    public Page<Study> searchStudiesAwaiting(Long reviewId, Long userId, Stage stage, StudySearchType searchType,
                                             String searchValue, int page, int size,
                                             SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return searchProcessor.searchAwaiting(searchType, reviewId, userId, stage, StudyState.TO_BE_REVIEWED, searchValue, pageRq);
    }

    public Page<Study> searchStudiesExcluded(Long reviewId, Stage stage, StudySearchType searchType,
                                             String searchValue, int page, int size,
                                             SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return searchProcessor.searchExcluded(searchType, reviewId, stage, StudyState.EXCLUDED, searchValue, pageRq);
    }

    public Page<Study> searchStudiesIncluded(Long reviewId, StudySearchType searchType,
                                             String searchValue, int page, int size,
                                             SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return searchProcessor.searchIncluded(searchType, reviewId, StudyState.INCLUDED, searchValue, pageRq);
    }

    public Page<Study> searchStudies(Long reviewId, StudySearchType searchType, String searchValue, int page, int size,
                                     SortProperty sortProperty, Sort.Direction sortDirection) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty.getName()));
        return searchProcessor.searchAll(searchType, reviewId, searchValue, pageRq);
    }
}
