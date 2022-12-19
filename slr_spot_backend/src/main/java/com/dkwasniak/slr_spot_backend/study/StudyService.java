package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
import com.dkwasniak.slr_spot_backend.study.exception.StudyNotFoundException;
import com.dkwasniak.slr_spot_backend.study.mapper.StudyMapper;
import com.dkwasniak.slr_spot_backend.study.status.StatusEnum;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public void removeStudyById(Long studyId) {
        studyRepository.deleteById(studyId);
    }

    public void saveAll(List<Study> studies) {
        studyRepository.saveAll(studies);
    }

    public List<Study> saveStudiesFromCsv(List<CSVRecord> records, String source) {
        List<Study> studies = StudyMapper.csvToStudies(records, source);
        saveAll(studies);
        return studies;
    }

    public List<Study> saveStudiesFromBib(BibTeXDatabase records) {
        List<Study> studies = StudyMapper.bibToStudies(records);
        saveAll(studies);
        return studies;
    }

    public List<Study> getStudiesToBeReviewed(Long reviewId, Long userId, int requiredReviewers, StatusEnum status) {
        return studyRepository.findAllToBeReviewed(reviewId, userId, requiredReviewers, status);
    }

    public List<Study> getStudiesReviewedByUserId(Long reviewId, Long userId) {
        return studyRepository.findAllReviewedByUserId(reviewId, userId);
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
}
