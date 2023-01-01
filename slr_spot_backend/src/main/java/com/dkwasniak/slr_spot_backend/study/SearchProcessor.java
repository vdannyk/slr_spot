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
//
//    public Page<Study> searchByState(StudyState state, StudySearchType searchType, Long reviewId, Long userId,
//                                     int requiredReviewers, StatusEnum status, String searchValue, Pageable pageable) {
//        switch (state) {
//            case TO_BE_REVIEWED -> {
//                return searchToBeReviewed(searchType, reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case CONFLICTED -> {
//                return searchConflicted(searchType, reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case AWAITING -> {
//                return searchAwaiting(searchType, reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case EXCLUDED -> {
//                return searchExcluded(searchType, reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            default -> {
//                throw new IllegalStateException("No studies found for given state");
//            }
//        }
//    }
//
//    private Page<Study> searchToBeReviewed(StudySearchType searchType, Long reviewId, Long userId, int requiredReviewers,
//                                           StatusEnum status, String searchValue, Pageable pageable) {
//        switch (searchType) {
//            case TITLE -> {
//                return studyRepository.findToBeReviewedByTitleContaining(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case AUTHORS -> {
//                return studyRepository.findToBeReviewedByAuthorsContaining(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case YEAR -> {
//                return studyRepository.findToBeReviewedByPublicationYearContaining(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_AUTHORS -> {
//                return studyRepository.findToBeReviewedByTitleAndAuthors(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_YEAR -> {
//                return studyRepository.findToBeReviewedByTitleAndYear(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case AUTHORS_YEAR -> {
//                return studyRepository.findToBeReviewedByAuthorsAndYear(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_AUTHORS_YEAR -> {
//                return studyRepository.findToBeReviewedByTitleAndAuthorsAndYear(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            default -> {
//                return studyRepository.findToBeReviewedByEverything(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//        }
//    }
//
//    private Page<Study> searchConflicted(StudySearchType searchType, Long reviewId, int requiredReviewers,
//                                           StatusEnum status, String searchValue, Pageable pageable) {
//        switch (searchType) {
//            case TITLE -> {
//                return studyRepository.findConflictedByTitleContaining(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case AUTHORS -> {
//                return studyRepository.findConflictedByAuthorsContaining(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case YEAR -> {
//                return studyRepository.findConflictedByPublicationYearContaining(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_AUTHORS -> {
//                return studyRepository.findConflictedByTitleAndAuthors(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_YEAR -> {
//                return studyRepository.findConflictedByTitleAndYear(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case AUTHORS_YEAR -> {
//                return studyRepository.findConflictedByAuthorsAndYear(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_AUTHORS_YEAR -> {
//                return studyRepository.findConflictedByTitleAndAuthorsAndYear(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            default -> {
//                return studyRepository.findConflictedByEverything(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//        }
//    }
//
//    private Page<Study> searchAwaiting(StudySearchType searchType, Long reviewId, Long userId, int requiredReviewers,
//                                           StatusEnum status, String searchValue, Pageable pageable) {
//        switch (searchType) {
//            case TITLE -> {
//                return studyRepository.findAwaitingByTitleContaining(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case AUTHORS -> {
//                return studyRepository.findAwaitingByAuthorsContaining(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case YEAR -> {
//                return studyRepository.findAwaitingByPublicationYearContaining(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_AUTHORS -> {
//                return studyRepository.findAwaitingByTitleAndAuthors(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_YEAR -> {
//                return studyRepository.findAwaitingByTitleAndYear(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case AUTHORS_YEAR -> {
//                return studyRepository.findAwaitingByAuthorsAndYear(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_AUTHORS_YEAR -> {
//                return studyRepository.findAwaitingByTitleAndAuthorsAndYear(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//            default -> {
//                return studyRepository.findAwaitingByEverything(reviewId, userId, requiredReviewers, status, searchValue, pageable);
//            }
//        }
//    }
//
//    private Page<Study> searchExcluded(StudySearchType searchType, Long reviewId, int requiredReviewers,
//                                           StatusEnum status, String searchValue, Pageable pageable) {
//        switch (searchType) {
//            case TITLE -> {
//                return studyRepository.findExcludedByTitleContaining(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case AUTHORS -> {
//                return studyRepository.findExcludedByAuthorsContaining(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case YEAR -> {
//                return studyRepository.findExcludedByPublicationYearContaining(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_AUTHORS -> {
//                return studyRepository.findExcludedByTitleAndAuthors(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_YEAR -> {
//                return studyRepository.findExcludedByTitleAndYear(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case AUTHORS_YEAR -> {
//                return studyRepository.findExcludedByAuthorsAndYear(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            case TITLE_AUTHORS_YEAR -> {
//                return studyRepository.findExcludedByTitleAndAuthorsAndYear(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//            default -> {
//                return studyRepository.findExcludedByEverything(reviewId, requiredReviewers, status, searchValue, pageable);
//            }
//        }
//    }
}
