package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.operation.Operation;
import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
import com.dkwasniak.slr_spot_backend.study.dto.IdentificationDto;
import com.dkwasniak.slr_spot_backend.study.dto.StatusDto;
import com.dkwasniak.slr_spot_backend.study.exception.StudyNotFoundException;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<Study> getStudiesToBeReviewed(Long reviewId, Long userId, Stage stage, Pageable pageable) {
        return studyRepository.findAllToBeReviewed(reviewId, userId, stage, StudyState.TO_BE_REVIEWED, pageable);
    }

    public Page<Study> getStudiesAwaiting(Long reviewId, Long userId, Stage stage, Pageable pageable) {
        return studyRepository.findAllAwaiting(reviewId, userId, stage, StudyState.TO_BE_REVIEWED, pageable);
    }

    public Page<Study> getStudiesByState(Long reviewId, StudyState state, Pageable pageable) {
        return studyRepository.findAllByStudyImport_Review_IdAndState(reviewId, state, pageable);
    }

    public List<Study> getStudiesListByState(Long reviewId, StudyState state) {
        return studyRepository.findAllByStudyImport_Review_IdAndState(reviewId, state);
    }

    public List<Study> getStudiesListByStage(Long reviewId, Stage stage) {
        return studyRepository.findAllByStudyImport_Review_IdAndStage(reviewId, stage);
    }

    public Page<Study> getStudiesByStageAndState(Long reviewId, Stage stage, StudyState state, Pageable pageable) {
        return studyRepository.findAllByStudyImport_Review_IdAndStageAndState(reviewId, stage, state, pageable);
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
        Stage stage = study.getStage();
        StudyState state = study.getState();
        return Stage.TITLE_ABSTRACT.equals(stage)
                || Stage.FULL_TEXT.equals(stage)
                && !StudyState.DUPLICATES.equals(state)
                && !StudyState.EXCLUDED.equals(state);
    }

    @Transactional
    public void addScreeningDecisionToStudy(Study study, ScreeningDecision screeningDecision) {
        study.addScreeningDecision(screeningDecision);
        studyRepository.save(study);
    }

    public StatusDto verifyStudyStatus(Study study, int requiredReviewers) {
        StudyState currentState = study.getState();
        Stage stage = study.getStage();
        List<ScreeningDecision> screeningDecisions = study.getScreeningDecisions();

        if (screeningDecisions.stream().filter(sd -> Decision.INCLUDE.equals(sd.getDecision())).count() >= requiredReviewers) {
            if (Stage.TITLE_ABSTRACT.equals(stage)) {
                return StatusDto.of(Stage.FULL_TEXT, StudyState.TO_BE_REVIEWED);
            } else {
                return StatusDto.of(Stage.FULL_TEXT, StudyState.INCLUDED);
            }
        } else if (screeningDecisions.stream().filter(sd -> Decision.EXCLUDE.equals(sd.getDecision())).count() >= requiredReviewers) {
            return StatusDto.of(stage, StudyState.EXCLUDED);
        } else if (screeningDecisions.size() >= requiredReviewers) {
            return StatusDto.of(stage, StudyState.CONFLICTED);
        } else {
            return StatusDto.of(stage, currentState);
        }
    }

    @Transactional
    public void updateStudyStatus(Study study, StatusDto statusDto) {
        if (!study.getStage().equals(statusDto.getStage())) {
            study.setStage(statusDto.getStage());
        }
        if (!study.getState().equals(statusDto.getState())) {
            study.setState(statusDto.getState());
        }
        studyRepository.save(study);
    }

    @Transactional
    public void clearDecisions(Study study) {
        List<ScreeningDecision> toRemove = new ArrayList<>();
        if (Stage.TITLE_ABSTRACT.equals(study.getStage())) {
            study.getScreeningDecisions().stream()
                    .filter(sd -> Stage.TITLE_ABSTRACT.equals(sd.getStage()))
                    .forEach(toRemove::add);
        } else {
            study.getScreeningDecisions().stream()
                    .filter(sd -> Stage.FULL_TEXT.equals(sd.getStage()))
                    .forEach(toRemove::add);
        }
        study.getScreeningDecisions().removeAll(toRemove);
        studyRepository.save(study);
    }

    public Study updateStudy(Study study) {
        return studyRepository.save(study);
    }

    public int getStudiesCountByStage(long reviewId, Stage stage) {
        return studyRepository.countAllByStudyImport_Review_IdAndStage(reviewId, stage);
    }

    public int getStudiesCountByState(long reviewId, StudyState state) {
        return studyRepository.countAllByStudyImport_Review_IdAndState(reviewId, state);
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

//    public Page<Study> getStudiesToBeReviewedByFolderId(Long reviewId, Long folderId, Long userId,
//                                                        int requiredReviewers, StatusEnum status, Pageable pageable) {
//        return studyRepository.findAllToBeReviewedByFolderId(reviewId, folderId, userId, requiredReviewers, status, pageable);
//    }
//
//    public Page<Study> getStudiesConflictedByFolderId(Long reviewId, Long folderId, int requiredReviewers,
//                                                      StatusEnum status, Pageable pageable) {
//        return studyRepository.findAllConflictedByFolderId(reviewId, folderId, requiredReviewers, status, pageable);
//    }
//
//    public Page<Study> getStudiesAwaitingByFolderId(Long reviewId, Long folderId, Long userId,
//                                                    int requiredReviewers, StatusEnum status, Pageable pageable) {
//        return studyRepository.findAllAwaitingByFolderId(reviewId, folderId, userId, requiredReviewers, status, pageable);
//    }
//
//    public Page<Study> getStudiesExcludedByFolderId(Long reviewId, Long folderId, int requiredReviewers,
//                                                    StatusEnum status, Pageable pageable) {
//        return studyRepository.findAllExcludedByFolderId(reviewId, folderId, requiredReviewers, status, pageable);
//    }

}
