package com.dkwasniak.slr_spot_backend.imports;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImportRepository extends JpaRepository<Import, Long> {

    Page<Import> findByReview_Id(long reviewId, Pageable pageable);
}
