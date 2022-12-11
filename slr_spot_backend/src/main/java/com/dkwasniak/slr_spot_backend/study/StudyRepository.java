package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    List<Study> findAllByStudyImport_Review(Review studyImport_review);
    List<Study> findAllByStudyImport_Review_AndScreeningDecisions_Empty(Review studyImport_review);
}
