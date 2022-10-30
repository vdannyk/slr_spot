package com.dkwasniak.slr_spot_backend.project;

import com.dkwasniak.slr_spot_backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByTitle(String title);
    List<Review> findByUser(User user);
}
