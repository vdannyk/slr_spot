package com.dkwasniak.slr_spot_backend.imports;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ImportRepository extends JpaRepository<Import, Long> {

    Set<Import> findByReview_Id(long reviewId);
}
