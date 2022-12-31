package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.comment.CommentService;
import com.dkwasniak.slr_spot_backend.comment.dto.CommentDto;
import com.dkwasniak.slr_spot_backend.comment.dto.CommentRequest;
import com.dkwasniak.slr_spot_backend.document.Document;
import com.dkwasniak.slr_spot_backend.file.FileService;
import com.dkwasniak.slr_spot_backend.imports.Import;
import com.dkwasniak.slr_spot_backend.imports.ImportService;
import com.dkwasniak.slr_spot_backend.operation.Operation;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningService;
import com.dkwasniak.slr_spot_backend.screeningDecision.dto.ScreeningDecisionDto;
import com.dkwasniak.slr_spot_backend.study.mapper.StudyMapper;
import com.dkwasniak.slr_spot_backend.study.status.StatusEnum;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.tag.TagService;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
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
    private final ImportService importService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final TagService tagService;
    private final CommentService commentService;
    private final ScreeningService screeningService;

    public Page<Study> getStudiesByReviewId(Long reviewId, int page, int size) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));
        return studyService.getStudiesByReviewId(reviewId, pageRq);
    }

    public void removeStudyById(Long studyId) {
        studyService.removeStudyById(studyId);
    }

    public List<Study> getStudiesToBeReviewed(Long reviewId, Long userId, StatusEnum status) {
        Review review = reviewService.getReviewById(reviewId);
        int requiredReviewers = review.getScreeningReviewers();
        return studyService.getStudiesToBeReviewed(reviewId, userId, requiredReviewers, status);
    }

    public List<Study> getStudiesConflicted(Long reviewId, StatusEnum status) {
        Review review = reviewService.getReviewById(reviewId);
        int requiredReviewers = review.getScreeningReviewers();
        return studyService.getStudiesConflicted(reviewId, requiredReviewers, status);
    }

    public List<Study> getStudiesAwaiting(Long reviewId, Long userId, StatusEnum status) {
        Review review = reviewService.getReviewById(reviewId);
        int requiredReviewers = review.getScreeningReviewers();
        return studyService.getStudiesAwaiting(reviewId, userId, requiredReviewers, status);
    }

    public List<Study> getStudiesExcluded(Long reviewId, StatusEnum status) {
        Review review = reviewService.getReviewById(reviewId);
        int requiredReviewers = review.getScreeningReviewers();
        return studyService.getStudiesExcluded(reviewId, requiredReviewers, status);
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

            Optional<ScreeningDecision> oldDecision = user.getScreeningDecisions().stream().filter(sd -> Objects.equals(sd.getStudy().getId(), studyId)).findFirst();
            if (oldDecision.isPresent()) {
                screeningService.updateDecision(oldDecision.get(), screeningDecisionDto.getDecision());
                studyService.addOperation(study,
                        new Operation(String.format(OperationDescription.CHANGE_VOTE.getDescription(), user.getEmail())));
            } else {
                ScreeningDecision screeningDecision = new ScreeningDecision(user, study, screeningDecisionDto.getDecision());
                studyService.addScreeningDecisionToStudy(study, screeningDecision);
                studyService.addOperation(study,
                        new Operation(String.format(OperationDescription.VOTE.getDescription(), user.getEmail())));
            }
            StatusEnum newStatus = studyService.verifyStudyStatus(study, requiredReviewers);
            if (!newStatus.equals(study.getStatus())) {
                studyService.updateStudyStatus(study, newStatus);
                studyService.addOperation(study,
                        new Operation(String.format(OperationDescription.CHANGE_STATUS.getDescription(), newStatus.name())));
            }
        }
    }

    public Decision getScreeningDecisionByUser(Long studyId, Long userId) {
        return screeningService.getScreeningDecisionByStudyIdAndUserId(studyId, userId).getDecision();
    }

    public void restoreStudy(Long studyId, StatusEnum status) {
        Study study = studyService.getStudyById(studyId);
        if (status != null) {
            studyService.updateStudyStatus(study, status);
        } else {
            studyService.clearDecisions(study);
        }
        studyService.addOperation(study, new Operation(OperationDescription.RESTORE_TO_SCREENING.getDescription()));
    }

    public void markStudyAsDuplicate(Long studyId) {
        Study study = studyService.getStudyById(studyId);
        studyService.updateStudyStatus(study, StatusEnum.DUPLICATES);
        studyService.addOperation(study, new Operation(OperationDescription.MARK_DUPLICATE.getDescription()));
    }

    public List<Study> getDuplicates(Long reviewId) {
        return studyService.getDuplicates(reviewId);
    }

    public List<Study> getIncludedStudies(Long reviewId) {
        return studyService.getIncludedStudies(reviewId);
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
        studyService.updateStudy(study);
        studyService.addOperation(study,
                new Operation(OperationDescription.REMOVE_FULLTEXT.getDescription()));
    }

    public int getStudiesCountByStatus(Long reviewId, StatusEnum statusEnum) {
        return studyService.getStudiesCountByStatus(reviewId, statusEnum);
    }

    public InputStreamResource exportStudiesByStatus(Long reviewId, StatusEnum statusEnum, String format) {
        fileService.checkIfExportFileFormatAllowed(format);
        List<Study> studiesToExport = studyService.getStudiesByReviewIdAndStatus(reviewId, statusEnum);
        return fileService.write(studiesToExport, format);
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
                                                        StatusEnum status, int page, int size) {
        Review review = reviewService.getReviewById(reviewId);
        int requiredReviewers = review.getScreeningReviewers();
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));
        return studyService.getStudiesToBeReviewedByFolderId(reviewId, folderId, userId, requiredReviewers, status, pageRq);
    }

    public Page<Study> getStudiesConflictedByFolderId(Long reviewId, Long folderId, StatusEnum status,
                                                      int page, int size) {
        Review review = reviewService.getReviewById(reviewId);
        int requiredReviewers = review.getScreeningReviewers();
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));
        return studyService.getStudiesConflictedByFolderId(reviewId, folderId, requiredReviewers, status, pageRq);
    }

    public Page<Study> getStudiesAwaitingByFolderId(Long reviewId, Long folderId, Long userId,
                                                    StatusEnum status, int page, int size) {
        Review review = reviewService.getReviewById(reviewId);
        int requiredReviewers = review.getScreeningReviewers();
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));
        return studyService.getStudiesAwaitingByFolderId(reviewId, folderId, userId, requiredReviewers, status, pageRq);
    }

    public Page<Study> getStudiesExcludedByFolderId(Long reviewId, Long folderId, StatusEnum status,
                                                    int page, int size) {
        Review review = reviewService.getReviewById(reviewId);
        int requiredReviewers = review.getScreeningReviewers();
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));
        return studyService.getStudiesExcludedByFolderId(reviewId, folderId, requiredReviewers, status, pageRq);
    }

    public Page<Study> searchByTitle(Long reviewId, String searchValue, int page, int size) {
        Pageable pageRq = PageRequest.of(page, size, Sort.by("title"));

        var field = "XD";
        switch (field) {
            case "TITLE" -> {
                return studyService.searchByTitle(reviewId, searchValue, pageRq);
            }
            case "AUTHORS" -> {
                return studyService.searchByAuthors(reviewId, searchValue, pageRq);
            }
            case "YEAR" -> {
                return studyService.searchByPublicationYear(reviewId, searchValue, pageRq);
            }
            case "TITLE_AUTHORS" -> {
                return studyService.searchByTitleAndAuthors(reviewId, searchValue, pageRq);
            }
            case "TITLE_YEAR" -> {
                return studyService.searchByTitleAndPublicationYear(reviewId, searchValue, pageRq);
            }
            case "AUTHORS_YEAR" -> {
                return studyService.searchByAuthorsAndPublicationYear(reviewId, searchValue, pageRq);
            }
            default -> {
                return studyService.searchByTitleAndAuthorsAndPublicationYear(reviewId, searchValue, pageRq);
            }
        }
    }

}
