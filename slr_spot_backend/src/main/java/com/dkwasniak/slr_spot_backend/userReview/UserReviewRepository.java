package com.dkwasniak.slr_spot_backend.userReview;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, UserReviewId> {

    Set<UserReview> findByReview_Id(long reviewId);
}
