package com.dkwasniak.slr_spot_backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Set<User> findByReviews_Review_Id(long reviewId);
    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.isActivated = TRUE " +
            "WHERE u.email = ?1")
    void enableUser(String email);
    Boolean existsByEmail(String email);
    @Query("SELECT u.email " +
            "FROM User u ")
    Set<String> getEmails();
    @Query("SELECT u.email " +
            "FROM User u " +
            "WHERE u.email <> :currentEmail " +
            "and not exists (" +
            "    SELECT u.email " +
            "    FROM User u" +
            "             LEFT JOIN UserReview ur " +
            "                       ON u.id = ur.user.id " +
            "    WHERE ur.review.id = :currentReview" +
            ")")
    Set<String> getAllowedEmails(@Param("currentEmail") String currentUserEmail,
                                 @Param("currentReview") long currentReviewId);
}
