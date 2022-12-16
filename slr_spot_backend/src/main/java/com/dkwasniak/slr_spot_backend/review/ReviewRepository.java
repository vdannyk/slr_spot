package com.dkwasniak.slr_spot_backend.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByTitle(String title);
    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.isPublic = true")
    Page<Review> findAllPublic(Pageable pageable);
    Page<Review> findByUsers_User_Id(long id, Pageable pageable);
}
