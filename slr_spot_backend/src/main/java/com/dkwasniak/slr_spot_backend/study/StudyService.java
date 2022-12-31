package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.operation.Operation;
import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
import com.dkwasniak.slr_spot_backend.study.dto.IdentificationDto;
import com.dkwasniak.slr_spot_backend.study.exception.StudyNotFoundException;
import com.dkwasniak.slr_spot_backend.study.mapper.StudyMapper;
import com.dkwasniak.slr_spot_backend.study.status.StatusEnum;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public Study getStudyById(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException("Study not found"));
    }

    public Page<Study> getStudiesByReviewId(Long reviewId, Pageable pageable) {
        return studyRepository.findAllByStudyImport_Review_Id(reviewId, pageable);
    }

    public void removeStudyById(Long studyId) {
        studyRepository.deleteById(studyId);
    }

    public void saveAll(List<Study> studies) {
        studyRepository.saveAll(studies);
    }

    public List<Study> getStudiesToBeReviewed(Long reviewId, Long userId, int requiredReviewers, StatusEnum status) {
        return studyRepository.findAllToBeReviewed(reviewId, userId, requiredReviewers, status);
    }

    public List<Study> getStudiesConflicted(Long reviewId, int requiredReviewers, StatusEnum status) {
        return studyRepository.findAllConflicted(reviewId, requiredReviewers, status);
    }

    public List<Study> getStudiesAwaiting(Long reviewId, Long userId, int requiredReviewers, StatusEnum status) {
        return studyRepository.findAllAwaiting(reviewId, userId, requiredReviewers, status);
    }

    public List<Study> getStudiesExcluded(Long reviewId, int requiredReviewers, StatusEnum status) {
        return studyRepository.findAllExcluded(reviewId, requiredReviewers, status);
    }

    public Set<Tag> getStudyTags(Study study) {
        return study.getTags();
    }

    public void addTagToStudy(Study study, Tag tag) {
        study.addTag(tag);
        studyRepository.save(study);
    }

    public void removeTagFromStudy(Study study, Tag tag) {
        study.removeTag(tag);
        studyRepository.save(study);
    }

    public List<Comment> getStudyComments(Study study) {
        return study.getComments();
    }

    public void addCommentToStudy(Study study, Comment comment) {
        study.addComment(comment);
        studyRepository.save(study);
    }

    public boolean isStudyScreeningAllowed(Study study) {
        StatusEnum status = study.getStatus();
        return StatusEnum.TITLE_ABSTRACT.equals(status) || StatusEnum.FULL_TEXT.equals(status);
    }

    @Transactional
    public void addScreeningDecisionToStudy(Study study, ScreeningDecision screeningDecision) {
        study.addScreeningDecision(screeningDecision);
        studyRepository.save(study);
    }

    public StatusEnum verifyStudyStatus(Study study, int requiredReviewers) {
        StatusEnum currentStatus = study.getStatus();
        List<ScreeningDecision> screeningDecisions = study.getScreeningDecisions();
        if (screeningDecisions.stream().filter(sd -> Decision.INCLUDE.equals(sd.getDecision())).count() >= requiredReviewers) {
            if (StatusEnum.TITLE_ABSTRACT.equals(currentStatus)) {
                return StatusEnum.FULL_TEXT;
            } else if (StatusEnum.FULL_TEXT.equals(currentStatus)) {
                return StatusEnum.INCLUDED;
            } else {
                return currentStatus;
            }
        } else {
            return currentStatus;
        }
    }

    @Transactional
    public void updateStudyStatus(Study study, StatusEnum statusEnum) {
        study.setStatus(statusEnum);
        study.getScreeningDecisions().clear();
        studyRepository.save(study);
    }

    @Transactional
    public void clearDecisions(Study study) {
        study.getScreeningDecisions().clear();
        studyRepository.save(study);
    }

    public List<Study> getDuplicates(Long reviewId) {
        return studyRepository.findAllDuplicates(reviewId);
    }

    public List<Study> getIncludedStudies(Long reviewId) {
        return studyRepository.findAllIncluded(reviewId);
    }

    public Study updateStudy(Study study) {
        return studyRepository.save(study);
    }

    public int getStudiesCountByStatus(long reviewId, StatusEnum status) {
        if (StatusEnum.EXCLUDED.equals(status)) {
            return 0;
        }
        return studyRepository.findStudiesCountByStatus(reviewId, status);
    }

    public List<Study> getStudiesByReviewIdAndStatus(long reviewId, StatusEnum status) {
        if (StatusEnum.EXCLUDED.equals(status)) {
            return new ArrayList<>();
        }
        List<Study> studies = studyRepository.findStudiesByReviewIdAndStatus(reviewId, status);
        if (CollectionUtils.isEmpty(studies)) {
            throw new IllegalStateException("No studies to export");
        }
        return studies;
    }

    public Set<String> getStudiesDoiByReviewId(Long reviewId) {
        return studyRepository.findAllStudiesDoiByReviewId(reviewId);
    }

    public Set<IdentificationDto> getStudyBasicInfoByReviewId(Long reviewId) {
        Set<TitleAndAuthorsAndPublicationYear> studiesWithBasicInfo = studyRepository.findAllBasicInfoByReviewId(reviewId);
        return studiesWithBasicInfo.stream().map(s -> IdentificationDto.builder().title(s.getTitle()).authors(s.getAuthors()).publicationYear(s.getPublicationYear()).build()).collect(Collectors.toSet());
    }

    @Transactional
    public void addOperation(Study study, Operation operation) {
        study.addOperation(operation);
        studyRepository.save(study);
    }

    public List<Operation> getStudyHistory(Study study) {
        return study.getOperations();
    }

    public Page<Study> getStudiesByFolderId(Long folderId, Long reviewId, Pageable pageable) {
        return studyRepository.findAllByStudyImport_Review_IdAndFolder_Id(reviewId, folderId, pageable);
    }

    public Page<Study> getStudiesToBeReviewedByFolderId(Long reviewId, Long folderId, Long userId,
                                                        int requiredReviewers, StatusEnum status, Pageable pageable) {
        return studyRepository.findAllToBeReviewedByFolderId(reviewId, folderId, userId, requiredReviewers, status, pageable);
    }

    public Page<Study> getStudiesConflictedByFolderId(Long reviewId, Long folderId, int requiredReviewers,
                                                      StatusEnum status, Pageable pageable) {
        return studyRepository.findAllConflictedByFolderId(reviewId, folderId, requiredReviewers, status, pageable);
    }

    public Page<Study> getStudiesAwaitingByFolderId(Long reviewId, Long folderId, Long userId,
                                                    int requiredReviewers, StatusEnum status, Pageable pageable) {
        return studyRepository.findAllAwaitingByFolderId(reviewId, folderId, userId, requiredReviewers, status, pageable);
    }

    public Page<Study> getStudiesExcludedByFolderId(Long reviewId, Long folderId, int requiredReviewers,
                                                    StatusEnum status, Pageable pageable) {
        return studyRepository.findAllExcludedByFolderId(reviewId, folderId, requiredReviewers, status, pageable);
    }

    public Page<Study> searchByTitle(Long reviewId, String searchValue, Pageable pageable) {
        return studyRepository.findByStudyImport_Review_IdAndTitleContaining(reviewId, searchValue, pageable);
    }

    public Page<Study> searchByAuthors(Long reviewId, String searchValue, Pageable pageable) {
        return studyRepository.findByStudyImport_Review_IdAndAuthorsContaining(reviewId, searchValue, pageable);
    }

    public Page<Study> searchByPublicationYear(Long reviewId, String searchValue, Pageable pageable) {
        return studyRepository.findByStudyImport_Review_IdAndPublicationYearContaining(reviewId, searchValue, pageable);
    }

    public Page<Study> searchByTitleAndAuthors(Long reviewId, String searchValue, Pageable pageable) {
        return studyRepository.findByStudyImport_Review_IdAndTitleContainingOrAuthorsContaining(reviewId, searchValue, pageable);
    }

    public Page<Study> searchByTitleAndPublicationYear(Long reviewId, String searchValue, Pageable pageable) {
        return studyRepository.findByStudyImport_Review_IdAndTitleContainingPublicationYearContaining(reviewId, searchValue, pageable);
    }

    public Page<Study> searchByAuthorsAndPublicationYear(Long reviewId, String searchValue, Pageable pageable) {
        return studyRepository.findByStudyImport_Review_IdAndAuthorsContainingPublicationYearContaining(reviewId, searchValue, pageable);
    }

    public Page<Study> searchByTitleAndAuthorsAndPublicationYear(Long reviewId, String searchValue, Pageable pageable) {
        return studyRepository.findByStudyImport_Review_IdAndTitleContainingAuthorsContainingPublicationYearContaining(reviewId, searchValue, pageable);
    }



}
