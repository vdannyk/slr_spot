package com.dkwasniak.slr_spot_backend.project;

import com.dkwasniak.slr_spot_backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByTitle(String title);
    List<Project> findByUser(User user);
}
