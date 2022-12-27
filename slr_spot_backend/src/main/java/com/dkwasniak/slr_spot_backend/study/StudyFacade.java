package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.comment.CommentService;
import com.dkwasniak.slr_spot_backend.comment.dto.CommentDto;
import com.dkwasniak.slr_spot_backend.comment.dto.CommentRequest;
import com.dkwasniak.slr_spot_backend.document.Document;
import com.dkwasniak.slr_spot_backend.file.FileService;
import com.dkwasniak.slr_spot_backend.imports.Import;
import com.dkwasniak.slr_spot_backend.imports.ImportService;
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

    public List<Study> getStudiesByReviewId(Long reviewId) {
        Set<Import> imports = importService.getImportsByReviewId(reviewId);
        List<Study> studies = new ArrayList<>();
        for (var studyImport : imports) {
            studies.addAll(studyImport.getStudies());
        }
        return studies;
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
        studyService.addTagToStudy(study, tag);
    }

    public void removeTagFromStudy(Long studyId, Long tagId) {
        Study study = studyService.getStudyById(studyId);
        Tag tag = tagService.getTagById(tagId);
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
    }

    @Transactional
    public void addStudyScreeningDecision(Long studyId, ScreeningDecisionDto screeningDecisionDto) {
        Review review = reviewService.getReviewById(screeningDecisionDto.getReviewId());
        int requiredReviewers = review.getScreeningReviewers();
        Study study = studyService.getStudyById(studyId);
        if (studyService.isStudyScreeningAllowed(study)) {
            User user = userService.getUserById(screeningDecisionDto.getUserId());

            Optional<ScreeningDecision> oldDecision = user.getScreeningDecisions().stream().filter(sd -> Objects.equals(sd.getStudy().getId(), studyId)).findFirst();
            if (oldDecision.isPresent()) {
                screeningService.updateDecision(oldDecision.get(), screeningDecisionDto.getDecision());
            } else {
                ScreeningDecision screeningDecision = new ScreeningDecision(user, study, screeningDecisionDto.getDecision());
                studyService.addScreeningDecisionToStudy(study, screeningDecision);
            }
            StatusEnum newStatus = studyService.verifyStudyStatus(study, requiredReviewers);
            if (!newStatus.equals(study.getStatus())) {
                studyService.updateStudyStatus(study, newStatus);
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
    }

    public void markStudyAsDuplicate(Long studyId) {
        Study study = studyService.getStudyById(studyId);
        studyService.updateStudyStatus(study, StatusEnum.DUPLICATES);
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
        return document;
    }

    public void deleteFullTextDocument(Long studyId) {
        Study study = studyService.getStudyById(studyId);
        study.setFullText(null);
        studyService.updateStudy(study);
    }

    public int getStudiesCountByStatus(Long reviewId, StatusEnum statusEnum) {
        return studyService.getStudiesCountByStatus(reviewId, statusEnum);
    }
}
