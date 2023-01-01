package com.dkwasniak.slr_spot_backend.study;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchProcessor {

    private final StudyRepository studyRepository;

    public Page<Study> searchAll(StudySearchType searchType, Long reviewId, String searchValue, Pageable pageable) {
        switch (searchType) {
            case TITLE -> {
                return studyRepository.findByStudyImport_Review_IdAndTitleContaining(reviewId, searchValue, pageable);
            }
            case AUTHORS -> {
                return studyRepository.findByStudyImport_Review_IdAndAuthorsContaining(reviewId, searchValue, pageable);
            }
            case YEAR -> {
                return studyRepository.findByStudyImport_Review_IdAndPublicationYearContaining(reviewId, searchValue, pageable);
            }
            case TITLE_AUTHORS -> {
                return studyRepository.findByStudyImport_Review_IdAndTitleContainingOrAuthorsContaining(reviewId, searchValue, pageable);
            }
            case TITLE_YEAR -> {
                return studyRepository.findByStudyImport_Review_IdAndTitleContainingPublicationYearContaining(reviewId, searchValue, pageable);
            }
            case AUTHORS_YEAR -> {
                return studyRepository.findByStudyImport_Review_IdAndAuthorsContainingPublicationYearContaining(reviewId, searchValue, pageable);
            }
            default -> {
                return studyRepository.findByStudyImport_Review_IdAndTitleContainingAuthorsContainingPublicationYearContaining(reviewId, searchValue, pageable);
            }
        }
    }


    public Page<Study> searchToBeReviewed(StudySearchType searchType, Long reviewId, Long userId,
                                           Stage stage, StudyState state, String searchValue, Pageable pageable) {
        switch (searchType) {
            case TITLE -> {
                return studyRepository.findToBeReviewedByTitleContaining(reviewId, userId, stage, state, searchValue, pageable);
            }
            case AUTHORS -> {
                return studyRepository.findToBeReviewedByAuthorsContaining(reviewId, userId, stage, state, searchValue, pageable);
            }
            case YEAR -> {
                return studyRepository.findToBeReviewedByPublicationYearContaining(reviewId, userId, stage, state, searchValue, pageable);
            }
            case TITLE_AUTHORS -> {
                return studyRepository.findToBeReviewedByTitleAndAuthors(reviewId, userId, stage, state, searchValue, pageable);
            }
            case TITLE_YEAR -> {
                return studyRepository.findToBeReviewedByTitleAndYear(reviewId, userId, stage, state, searchValue, pageable);
            }
            case AUTHORS_YEAR -> {
                return studyRepository.findToBeReviewedByAuthorsAndYear(reviewId, userId, stage, state, searchValue, pageable);
            }
            case TITLE_AUTHORS_YEAR -> {
                return studyRepository.findToBeReviewedByTitleAndAuthorsAndYear(reviewId, userId, stage, state, searchValue, pageable);
            }
            default -> {
                return studyRepository.findToBeReviewedByEverything(reviewId, userId, stage, state, searchValue, pageable);
            }
        }
    }

    public Page<Study> searchConflicted(StudySearchType searchType, Long reviewId, Stage stage, StudyState state,
                                         String searchValue, Pageable pageable) {
        switch (searchType) {
            case TITLE -> {
                return studyRepository.findConflictedByTitleContaining(reviewId, stage, state, searchValue, pageable);
            }
            case AUTHORS -> {
                return studyRepository.findConflictedByAuthorsContaining(reviewId, stage, state, searchValue, pageable);
            }
            case YEAR -> {
                return studyRepository.findConflictedByPublicationYearContaining(reviewId, stage, state, searchValue, pageable);
            }
            case TITLE_AUTHORS -> {
                return studyRepository.findConflictedByTitleAndAuthors(reviewId, stage, state, searchValue, pageable);
            }
            case TITLE_YEAR -> {
                return studyRepository.findConflictedByTitleAndYear(reviewId, stage, state, searchValue, pageable);
            }
            case AUTHORS_YEAR -> {
                return studyRepository.findConflictedByAuthorsAndYear(reviewId, stage, state, searchValue, pageable);
            }
            case TITLE_AUTHORS_YEAR -> {
                return studyRepository.findConflictedByTitleAndAuthorsAndYear(reviewId, stage, state, searchValue, pageable);
            }
            default -> {
                return studyRepository.findConflictedByEverything(reviewId, stage, state, searchValue, pageable);
            }
        }
    }

    public Page<Study> searchAwaiting(StudySearchType searchType, Long reviewId, Long userId,
                                      Stage stage, StudyState state, String searchValue, Pageable pageable) {
        switch (searchType) {
            case TITLE -> {
                return studyRepository.findAwaitingByTitleContaining(reviewId, userId, stage, state, searchValue, pageable);
            }
            case AUTHORS -> {
                return studyRepository.findAwaitingByAuthorsContaining(reviewId, userId, stage, state, searchValue, pageable);
            }
            case YEAR -> {
                return studyRepository.findAwaitingByPublicationYearContaining(reviewId, userId, stage, state, searchValue, pageable);
            }
            case TITLE_AUTHORS -> {
                return studyRepository.findAwaitingByTitleAndAuthors(reviewId, userId, stage, state, searchValue, pageable);
            }
            case TITLE_YEAR -> {
                return studyRepository.findAwaitingByTitleAndYear(reviewId, userId, stage, state, searchValue, pageable);
            }
            case AUTHORS_YEAR -> {
                return studyRepository.findAwaitingByAuthorsAndYear(reviewId, userId, stage, state, searchValue, pageable);
            }
            case TITLE_AUTHORS_YEAR -> {
                return studyRepository.findAwaitingByTitleAndAuthorsAndYear(reviewId, userId, stage, state, searchValue, pageable);
            }
            default -> {
                return studyRepository.findAwaitingByEverything(reviewId, userId, stage, state, searchValue, pageable);
            }
        }
    }

    public Page<Study> searchExcluded(StudySearchType searchType, Long reviewId, Stage stage, StudyState state,
                                       String searchValue, Pageable pageable) {
        switch (searchType) {
            case TITLE -> {
                return studyRepository.findExcludedByTitleContaining(reviewId, stage, state, searchValue, pageable);
            }
            case AUTHORS -> {
                return studyRepository.findExcludedByAuthorsContaining(reviewId, stage, state, searchValue, pageable);
            }
            case YEAR -> {
                return studyRepository.findExcludedByPublicationYearContaining(reviewId, stage, state, searchValue, pageable);
            }
            case TITLE_AUTHORS -> {
                return studyRepository.findExcludedByTitleAndAuthors(reviewId, stage, state, searchValue, pageable);
            }
            case TITLE_YEAR -> {
                return studyRepository.findExcludedByTitleAndYear(reviewId, stage, state, searchValue, pageable);
            }
            case AUTHORS_YEAR -> {
                return studyRepository.findExcludedByAuthorsAndYear(reviewId, stage, state, searchValue, pageable);
            }
            case TITLE_AUTHORS_YEAR -> {
                return studyRepository.findExcludedByTitleAndAuthorsAndYear(reviewId, stage, state, searchValue, pageable);
            }
            default -> {
                return studyRepository.findExcludedByEverything(reviewId, stage, state, searchValue, pageable);
            }
        }
    }
}
