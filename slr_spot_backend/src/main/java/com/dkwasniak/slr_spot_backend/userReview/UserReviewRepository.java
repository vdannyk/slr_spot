package com.dkwasniak.slr_spot_backend.userReview;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, UserReviewId> {

    List<UserReview> findByReview_IdOrderByUser_LastName(long reviewId);
    Optional<UserReview> findByReview_IdAndUser_Id(long reviewId, long userId);
}
