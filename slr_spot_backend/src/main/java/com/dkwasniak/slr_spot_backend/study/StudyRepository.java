package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.study.status.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    List<Study> findAllByStudyImport_Review(Review studyImport_review);
    List<Study> findAllByStudyImport_Review_AndScreeningDecisions_Empty(Review studyImport_review);

    @Query("SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.status = :status " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.decision <> 'UNCLEAR') < :size " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.user.id = :userId) = 0 ")
    List<Study> findAllToBeReviewed(@Param("reviewId") long reviewId,
                                    @Param("userId") long userId,
                                    @Param("size") long size,
                                    @Param("status") StatusEnum status);

    @Query("SELECT s FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "LEFT OUTER JOIN ScreeningDecision sd " +
            "ON sd.study.id = s.id " +
            "WHERE i.review.id = :reviewId " +
            "AND sd.user.id = :userId")
    List<Study> findAllReviewedByUserId(@Param("reviewId") long reviewId, @Param("userId") long userId);

    @Query("SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "LEFT OUTER JOIN ScreeningDecision sd " +
            "ON sd.study.id = s.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.status = :status " +
            "AND sd.user.id = :userId " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.decision <> 'UNCLEAR') < :size ")
    List<Study> findAllAwaiting(@Param("reviewId") long reviewId,
                                @Param("userId") long userId,
                                @Param("size") long size,
                                @Param("status") StatusEnum status);

    @Query("SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.status = :status " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.decision = 'INCLUDE') = :size ")
    List<Study> findAllIncluded(@Param("reviewId") long reviewId,
                                @Param("size") long size,
                                @Param("status") StatusEnum status);

    @Query("SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.status = :status " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.decision = 'EXCLUDE') = :size ")
    List<Study> findAllExcluded(@Param("reviewId") long reviewId,
                                @Param("size") long size,
                                @Param("status") StatusEnum status);

    @Query("SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.status = :status " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.decision <> 'UNCLEAR') = :size " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.decision = 'INCLUDE') < :size " +
            "AND " +
            "(SELECT COUNT(sd) " +
            "FROM ScreeningDecision sd " +
            "WHERE sd.study.id = s.id AND sd.decision = 'EXCLUDE') < :size ")
    List<Study> findAllConflicted(@Param("reviewId") long reviewId,
                                  @Param("size") long size,
                                  @Param("status") StatusEnum status);

    @Query("SELECT s " +
            "FROM Study s " +
            "LEFT OUTER JOIN Import i " +
            "ON s.studyImport.id = i.id " +
            "WHERE i.review.id = :reviewId " +
            "AND s.status = 'DUPLICATES' ")
    List<Study> findAllDuplicates(@Param("reviewId") long reviewId);

}
