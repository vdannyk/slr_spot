package com.dkwasniak.slr_spot_backend.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByTitle(String title);
    List<Review> findByUsers_Email(String email);
    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.isPublic = true")
    List<Review> findAllPublic();
}
