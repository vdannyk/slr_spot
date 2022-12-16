package com.dkwasniak.slr_spot_backend.criterion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CriterionRepository extends JpaRepository<Criterion, Long> {

    List<Criterion> findByReview_Id(long reviewId);

}
