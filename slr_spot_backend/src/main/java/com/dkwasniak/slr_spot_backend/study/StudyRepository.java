package com.dkwasniak.slr_spot_backend.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    Page<Study> findAllByStudyImport_Review_Id(long reviewId, Pageable pageable);
    Page<Study> findAllByStudyImport_Review_IdAndFolder_Id(long reviewId, long folderId, Pageable pageable);

    Page<Study> findAllByStudyImport_Review_IdAndStageAndState(long reviewId, Stage stage, StudyState studyState, Pageable pageable);
    Page<Study> findAllByStudyImport_Review_IdAndState(long reviewId, StudyState studyState, Pageable pageable);
    List<Study> findAllByStudyImport_Review_IdAndState(long reviewId, StudyState studyState);
    List<Study> findAllByStudyImport_Review_IdAndStage(long reviewId, Stage stage);
    int countAllByStudyImport_Review_IdAndStage(long reviewId, Stage stage);
    int countAllByStudyImport_Review_IdAndState(long reviewId, StudyState state);
    Page<Study> findAllByStudyImport_Review_IdAndFolder_IdAndStageAndState(long reviewId, long folderId, Stage stage, StudyState studyState, Pageable pageable);

    @Query("SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.stage = :stage AND sd.user.id = :userId) = 0 ")
    Page<Study> findAllToBeReviewed(@Param("reviewId") long reviewId,
                                    @Param("userId") long userId,
                                    @Param("stage") Stage stage,
                                    @Param("state") StudyState state,
                                    Pageable pageable);
    @Query("SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.stage = :stage AND sd.user.id = :userId) = 0 " +
            "AND s.folder.id = :folderId ")
    Page<Study> findAllToBeReviewedByFolderId(@Param("reviewId") long reviewId,
                                              @Param("userId") long userId,
                                              @Param("folderId") long folderId,
                                              @Param("stage") Stage stage,
                                              @Param("state") StudyState state,
                                              Pageable pageable);

    @Query("SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.stage = :stage AND sd.user.id = :userId) > 0 ")
    Page<Study> findAllAwaiting(@Param("reviewId") long reviewId,
                                @Param("userId") long userId,
                                @Param("stage") Stage stage,
                                @Param("state") StudyState state,
                                Pageable pageable);
    @Query("SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.stage = :stage " +
            "AND s.state = :state " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.stage = :stage AND sd.user.id = :userId) > 0 " +
            "AND s.folder.id = :folderId ")
    Page<Study> findAllAwaitingByFolderId(@Param("reviewId") long reviewId,
                                          @Param("userId") long userId,
                                          @Param("folderId") long folderId,
                                          @Param("stage") Stage stage,
                                          @Param("state") StudyState state,
                                          Pageable pageable);

    // search methods
    Page<Study> findByStudyImport_Review_IdAndTitleContaining(long reviewId, String value, Pageable pageable);
    Page<Study> findByStudyImport_Review_IdAndAuthorsContaining(long reviewId, String value, Pageable pageable);
    @Query(StudyQueries.STUDIES_BY_REVIEW_ID_QUERY + StudyQueries.BY_PUBLICATION_YEAR_CONDITION)
    Page<Study> findByStudyImport_Review_IdAndPublicationYearContaining(@Param("reviewId") long reviewId,
                                                                        @Param("searchValue") String value,
                                                                        Pageable pageable);
    @Query(StudyQueries.STUDIES_BY_REVIEW_ID_QUERY + StudyQueries.BY_TITLE_AND_AUTHORS_CONDITION)
    Page<Study> findByStudyImport_Review_IdAndTitleContainingOrAuthorsContaining(@Param("reviewId") long reviewId,
                                                                                 @Param("searchValue") String value,
                                                                                 Pageable pageable);
    @Query(StudyQueries.STUDIES_BY_REVIEW_ID_QUERY + StudyQueries.BY_TITLE_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findByStudyImport_Review_IdAndTitleContainingPublicationYearContaining(@Param("reviewId") long reviewId,
                                                                                       @Param("searchValue") String value,
                                                                                       Pageable pageable);
    @Query(StudyQueries.STUDIES_BY_REVIEW_ID_QUERY + StudyQueries.BY_AUTHORS_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findByStudyImport_Review_IdAndAuthorsContainingPublicationYearContaining(@Param("reviewId") long reviewId,
                                                                                         @Param("searchValue") String value,
                                                                                         Pageable pageable);
    @Query(StudyQueries.STUDIES_BY_REVIEW_ID_QUERY + StudyQueries.BY_TITLE_AUTHORS_PUBLICATION_YEAR_CONDITION)
    Page<Study> findByStudyImport_Review_IdAndTitleContainingAuthorsContainingPublicationYearContaining(@Param("reviewId") long reviewId,
                                                                                                        @Param("searchValue") String value,
                                                                                                        Pageable pageable);


    @Query(StudyQueries.TO_BE_REVIEWED_QUERY + StudyQueries.BY_TITLE_CONDITION)
    Page<Study> findToBeReviewedByTitleContaining(@Param("reviewId") long reviewId,
                                                  @Param("userId") long userId,
                                                  @Param("stage") Stage stage,
                                                  @Param("state") StudyState state,
                                                  @Param("searchValue") String value,
                                                  Pageable pageable);
    @Query(StudyQueries.TO_BE_REVIEWED_QUERY + StudyQueries.BY_AUTHORS_CONDITION)
    Page<Study> findToBeReviewedByAuthorsContaining(@Param("reviewId") long reviewId,
                                                  @Param("userId") long userId,
                                                  @Param("stage") Stage stage,
                                                    @Param("state") StudyState state,
                                                  @Param("searchValue") String value,
                                                  Pageable pageable);
    @Query(StudyQueries.TO_BE_REVIEWED_QUERY + StudyQueries.BY_PUBLICATION_YEAR_CONDITION)
    Page<Study> findToBeReviewedByPublicationYearContaining(@Param("reviewId") long reviewId,
                                                  @Param("userId") long userId,
                                                  @Param("stage") Stage stage,
                                                @Param("state") StudyState state,
                                                  @Param("searchValue") String value,
                                                  Pageable pageable);
    @Query(StudyQueries.TO_BE_REVIEWED_QUERY + StudyQueries.BY_TITLE_AND_AUTHORS_CONDITION)
    Page<Study> findToBeReviewedByTitleAndAuthors(@Param("reviewId") long reviewId,
                                                @Param("userId") long userId,
                                                @Param("stage") Stage stage,
                                                  @Param("state") StudyState state,
                                                @Param("searchValue") String value,
                                                Pageable pageable);
    @Query(StudyQueries.TO_BE_REVIEWED_QUERY + StudyQueries.BY_TITLE_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findToBeReviewedByTitleAndYear(@Param("reviewId") long reviewId,
                                                  @Param("userId") long userId,
                                                  @Param("stage") Stage stage,
                                               @Param("state") StudyState state,
                                                  @Param("searchValue") String value,
                                                  Pageable pageable);
    @Query(StudyQueries.TO_BE_REVIEWED_QUERY + StudyQueries.BY_AUTHORS_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findToBeReviewedByAuthorsAndYear(@Param("reviewId") long reviewId,
                                               @Param("userId") long userId,
                                               @Param("stage") Stage stage,
                                                 @Param("state") StudyState state,
                                               @Param("searchValue") String value,
                                               Pageable pageable);
    @Query(StudyQueries.TO_BE_REVIEWED_QUERY + StudyQueries.BY_TITLE_AUTHORS_PUBLICATION_YEAR_CONDITION)
    Page<Study> findToBeReviewedByTitleAndAuthorsAndYear(@Param("reviewId") long reviewId,
                                                         @Param("userId") long userId,
                                                         @Param("stage") Stage stage,
                                                         @Param("state") StudyState state,
                                                         @Param("searchValue") String value,
                                                         Pageable pageable);
    @Query(StudyQueries.TO_BE_REVIEWED_TAGS_QUERY + StudyQueries.BY_EVERYTHING_CONDITION)
    Page<Study> findToBeReviewedByEverything(@Param("reviewId") long reviewId,
                                             @Param("userId") long userId,
                                             @Param("stage") Stage stage,
                                             @Param("state") StudyState state,
                                             @Param("searchValue") String value,
                                             Pageable pageable);

    @Query(StudyQueries.AWAITING_QUERY + StudyQueries.BY_TITLE_CONDITION)
    Page<Study> findAwaitingByTitleContaining(@Param("reviewId") long reviewId,
                                              @Param("userId") long userId,
                                              @Param("stage") Stage stage,
                                              @Param("state") StudyState state,
                                              @Param("searchValue") String value,
                                              Pageable pageable);
    @Query(StudyQueries.AWAITING_QUERY + StudyQueries.BY_AUTHORS_CONDITION)
    Page<Study> findAwaitingByAuthorsContaining(@Param("reviewId") long reviewId,
                                                @Param("userId") long userId,
                                                @Param("stage") Stage stage,
                                                @Param("state") StudyState state,
                                                @Param("searchValue") String value,
                                                Pageable pageable);
    @Query(StudyQueries.AWAITING_QUERY + StudyQueries.BY_PUBLICATION_YEAR_CONDITION)
    Page<Study> findAwaitingByPublicationYearContaining(@Param("reviewId") long reviewId,
                                                        @Param("userId") long userId,
                                                        @Param("stage") Stage stage,
                                                        @Param("state") StudyState state,
                                                        @Param("searchValue") String value,
                                                        Pageable pageable);
    @Query(StudyQueries.AWAITING_QUERY + StudyQueries.BY_TITLE_AND_AUTHORS_CONDITION)
    Page<Study> findAwaitingByTitleAndAuthors(@Param("reviewId") long reviewId,
                                              @Param("userId") long userId,
                                              @Param("stage") Stage stage,
                                              @Param("state") StudyState state,
                                              @Param("searchValue") String value,
                                              Pageable pageable);
    @Query(StudyQueries.AWAITING_QUERY + StudyQueries.BY_TITLE_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findAwaitingByTitleAndYear(@Param("reviewId") long reviewId,
                                           @Param("userId") long userId,
                                           @Param("stage") Stage stage,
                                           @Param("state") StudyState state,
                                           @Param("searchValue") String value,
                                           Pageable pageable);
    @Query(StudyQueries.AWAITING_QUERY + StudyQueries.BY_AUTHORS_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findAwaitingByAuthorsAndYear(@Param("reviewId") long reviewId,
                                             @Param("userId") long userId,
                                             @Param("stage") Stage stage,
                                             @Param("state") StudyState state,
                                             @Param("searchValue") String value,
                                             Pageable pageable);
    @Query(StudyQueries.AWAITING_QUERY + StudyQueries.BY_TITLE_AUTHORS_PUBLICATION_YEAR_CONDITION)
    Page<Study> findAwaitingByTitleAndAuthorsAndYear(@Param("reviewId") long reviewId,
                                                     @Param("userId") long userId,
                                                     @Param("stage") Stage stage,
                                                     @Param("state") StudyState state,
                                                     @Param("searchValue") String value,
                                                     Pageable pageable);
    @Query(StudyQueries.AWAITING_TAGS_QUERY + StudyQueries.BY_EVERYTHING_CONDITION)
    Page<Study> findAwaitingByEverything(@Param("reviewId") long reviewId,
                                         @Param("userId") long userId,
                                         @Param("stage") Stage stage,
                                         @Param("state") StudyState state,
                                         @Param("searchValue") String value,
                                         Pageable pageable);

    @Query(StudyQueries.EXCLUDED_QUERY + StudyQueries.BY_TITLE_CONDITION)
    Page<Study> findExcludedByTitleContaining(@Param("reviewId") long reviewId,
                                              @Param("stage") Stage stage,
                                              @Param("state") StudyState state,
                                              @Param("searchValue") String value,
                                              Pageable pageable);
    @Query(StudyQueries.EXCLUDED_QUERY + StudyQueries.BY_AUTHORS_CONDITION)
    Page<Study> findExcludedByAuthorsContaining(@Param("reviewId") long reviewId,
                                                @Param("stage") Stage stage,
                                                @Param("state") StudyState state,
                                                @Param("searchValue") String value,
                                                Pageable pageable);
    @Query(StudyQueries.EXCLUDED_QUERY + StudyQueries.BY_PUBLICATION_YEAR_CONDITION)
    Page<Study> findExcludedByPublicationYearContaining(@Param("reviewId") long reviewId,
                                                        @Param("stage") Stage stage,
                                                        @Param("state") StudyState state,
                                                        @Param("searchValue") String value,
                                                        Pageable pageable);
    @Query(StudyQueries.EXCLUDED_QUERY + StudyQueries.BY_TITLE_AND_AUTHORS_CONDITION)
    Page<Study> findExcludedByTitleAndAuthors(@Param("reviewId") long reviewId,
                                              @Param("stage") Stage stage,
                                              @Param("state") StudyState state,
                                              @Param("searchValue") String value,
                                              Pageable pageable);
    @Query(StudyQueries.EXCLUDED_QUERY + StudyQueries.BY_TITLE_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findExcludedByTitleAndYear(@Param("reviewId") long reviewId,
                                           @Param("stage") Stage stage,
                                           @Param("state") StudyState state,
                                           @Param("searchValue") String value,
                                           Pageable pageable);
    @Query(StudyQueries.EXCLUDED_QUERY + StudyQueries.BY_AUTHORS_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findExcludedByAuthorsAndYear(@Param("reviewId") long reviewId,
                                             @Param("stage") Stage stage,
                                             @Param("state") StudyState state,
                                             @Param("searchValue") String value,
                                             Pageable pageable);
    @Query(StudyQueries.EXCLUDED_QUERY + StudyQueries.BY_TITLE_AUTHORS_PUBLICATION_YEAR_CONDITION)
    Page<Study> findExcludedByTitleAndAuthorsAndYear(@Param("reviewId") long reviewId,
                                                     @Param("stage") Stage stage,
                                                     @Param("state") StudyState state,
                                                     @Param("searchValue") String value,
                                                     Pageable pageable);
    @Query(StudyQueries.EXCLUDED_TAGS_QUERY + StudyQueries.BY_EVERYTHING_CONDITION)
    Page<Study> findExcludedByEverything(@Param("reviewId") long reviewId,
                                         @Param("stage") Stage stage,
                                         @Param("state") StudyState state,
                                         @Param("searchValue") String value,
                                         Pageable pageable);

    @Query(StudyQueries.CONFLICTED_QUERY + StudyQueries.BY_TITLE_CONDITION)
    Page<Study> findConflictedByTitleContaining(@Param("reviewId") long reviewId,
                                                @Param("stage") Stage stage,
                                                @Param("state") StudyState state,
                                                @Param("searchValue") String value,
                                                Pageable pageable);
    @Query(StudyQueries.CONFLICTED_QUERY + StudyQueries.BY_AUTHORS_CONDITION)
    Page<Study> findConflictedByAuthorsContaining(@Param("reviewId") long reviewId,
                                                  @Param("stage") Stage stage,
                                                  @Param("state") StudyState state,
                                                  @Param("searchValue") String value,
                                                  Pageable pageable);
    @Query(StudyQueries.CONFLICTED_QUERY + StudyQueries.BY_PUBLICATION_YEAR_CONDITION)
    Page<Study> findConflictedByPublicationYearContaining(@Param("reviewId") long reviewId,
                                                          @Param("stage") Stage stage,
                                                          @Param("state") StudyState state,
                                                          @Param("searchValue") String value,
                                                          Pageable pageable);
    @Query(StudyQueries.CONFLICTED_QUERY + StudyQueries.BY_TITLE_AND_AUTHORS_CONDITION)
    Page<Study> findConflictedByTitleAndAuthors(@Param("reviewId") long reviewId,
                                                @Param("stage") Stage stage,
                                                @Param("state") StudyState state,
                                                @Param("searchValue") String value,
                                                Pageable pageable);
    @Query(StudyQueries.CONFLICTED_QUERY + StudyQueries.BY_TITLE_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findConflictedByTitleAndYear(@Param("reviewId") long reviewId,
                                             @Param("stage") Stage stage,
                                             @Param("state") StudyState state,
                                             @Param("searchValue") String value,
                                             Pageable pageable);
    @Query(StudyQueries.CONFLICTED_QUERY + StudyQueries.BY_AUTHORS_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findConflictedByAuthorsAndYear(@Param("reviewId") long reviewId,
                                               @Param("stage") Stage stage,
                                               @Param("state") StudyState state,
                                               @Param("searchValue") String value,
                                               Pageable pageable);
    @Query(StudyQueries.CONFLICTED_QUERY + StudyQueries.BY_TITLE_AUTHORS_PUBLICATION_YEAR_CONDITION)
    Page<Study> findConflictedByTitleAndAuthorsAndYear(@Param("reviewId") long reviewId,
                                                       @Param("stage") Stage stage,
                                                       @Param("state") StudyState state,
                                                       @Param("searchValue") String value,
                                                       Pageable pageable);
    @Query(StudyQueries.CONFLICTED_TAGS_QUERY + StudyQueries.BY_EVERYTHING_CONDITION)
    Page<Study> findConflictedByEverything(@Param("reviewId") long reviewId,
                                           @Param("stage") Stage stage,
                                           @Param("state") StudyState state,
                                           @Param("searchValue") String value,
                                           Pageable pageable);

    @Query(StudyQueries.INCLUDED_QUERY + StudyQueries.BY_TITLE_CONDITION)
    Page<Study> findIncludedByTitleContaining(@Param("reviewId") long reviewId,
                                              @Param("state") StudyState state,
                                              @Param("searchValue") String value,
                                              Pageable pageable);
    @Query(StudyQueries.INCLUDED_QUERY + StudyQueries.BY_AUTHORS_CONDITION)
    Page<Study> findIncludedByAuthorsContaining(@Param("reviewId") long reviewId,
                                                @Param("state") StudyState state,
                                                @Param("searchValue") String value,
                                                Pageable pageable);
    @Query(StudyQueries.INCLUDED_QUERY + StudyQueries.BY_PUBLICATION_YEAR_CONDITION)
    Page<Study> findIncludedByPublicationYearContaining(@Param("reviewId") long reviewId,
                                                        @Param("state") StudyState state,
                                                        @Param("searchValue") String value,
                                                        Pageable pageable);
    @Query(StudyQueries.INCLUDED_QUERY + StudyQueries.BY_TITLE_AND_AUTHORS_CONDITION)
    Page<Study> findIncludedByTitleAndAuthors(@Param("reviewId") long reviewId,
                                              @Param("state") StudyState state,
                                              @Param("searchValue") String value,
                                              Pageable pageable);
    @Query(StudyQueries.INCLUDED_QUERY + StudyQueries.BY_TITLE_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findIncludedByTitleAndYear(@Param("reviewId") long reviewId,
                                           @Param("state") StudyState state,
                                           @Param("searchValue") String value,
                                           Pageable pageable);
    @Query(StudyQueries.INCLUDED_QUERY + StudyQueries.BY_AUTHORS_AND_PUBLICATION_YEAR_CONDITION)
    Page<Study> findIncludedByAuthorsAndYear(@Param("reviewId") long reviewId,
                                             @Param("state") StudyState state,
                                             @Param("searchValue") String value,
                                             Pageable pageable);
    @Query(StudyQueries.INCLUDED_QUERY + StudyQueries.BY_TITLE_AUTHORS_PUBLICATION_YEAR_CONDITION)
    Page<Study> findIncludedByTitleAndAuthorsAndYear(@Param("reviewId") long reviewId,
                                                     @Param("state") StudyState state,
                                                     @Param("searchValue") String value,
                                                     Pageable pageable);
    @Query(StudyQueries.INCLUDED_TAGS_QUERY + StudyQueries.BY_EVERYTHING_CONDITION)
    Page<Study> findIncludedByEverything(@Param("reviewId") long reviewId,
                                         @Param("state") StudyState state,
                                         @Param("searchValue") String value,
                                         Pageable pageable);
//
//    @Query("SELECT s " +
//            "FROM Study s " +
//            "LEFT OUTER JOIN Import i " +
//            "ON s.studyImport.id = i.id " +
//            "WHERE i.review.id = :reviewId " +
//            "AND s.status = 'DUPLICATES' ")
//    List<Study> findAllDuplicates(@Param("reviewId") long reviewId);
//
//    @Query("SELECT s " +
//            "FROM Study s " +
//            "LEFT OUTER JOIN Import i " +
//            "ON s.studyImport.id = i.id " +
//            "WHERE i.review.id = :reviewId " +
//            "AND s.status = 'INCLUDED' ")
//    List<Study> findAllIncluded(@Param("reviewId") long reviewId);
//
//    @Query("SELECT COUNT(s) " +
//            "FROM Study s " +
//            "LEFT OUTER JOIN Import i " +
//            "ON s.studyImport.id = i.id " +
//            "WHERE i.review.id = :reviewId " +
//            "AND s.status = :status ")
//    int findStudiesCountByStatus(@Param("reviewId") long reviewId,
//                                 @Param("status") StatusEnum status);
//
//    @Query("SELECT s " +
//            "FROM Study s " +
//            "LEFT OUTER JOIN Import i " +
//            "ON s.studyImport.id = i.id " +
//            "WHERE i.review.id = :reviewId " +
//            "AND s.status = :status ")
//    List<Study> findStudiesByReviewIdAndStatus(@Param("reviewId") long reviewId,
//                                               @Param("status") StatusEnum status);
//
    @Query("SELECT s.doi " +
            "FROM Study s " +
            "WHERE s.studyImport.review.id = :reviewId AND s.doi IS NOT null ")
    Set<String> findAllStudiesDoiByReviewId(@Param("reviewId") long reviewId);

    @Query("SELECT s " +
            "FROM Study s " +
            "WHERE s.studyImport.review.id = :reviewId " +
            "AND s.authors IS NOT null " +
            "AND s.title IS NOT null " +
            "AND s.publicationYear IS NOT null ")
    Set<TitleAndAuthorsAndPublicationYear> findAllBasicInfoByReviewId(@Param("reviewId") long reviewId);
}
