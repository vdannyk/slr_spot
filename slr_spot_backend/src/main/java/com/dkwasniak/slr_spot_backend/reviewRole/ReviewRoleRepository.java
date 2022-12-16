package com.dkwasniak.slr_spot_backend.reviewRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRoleRepository extends JpaRepository<ReviewRole, Long> {

    Optional<ReviewRole> findByName(String name);
}
