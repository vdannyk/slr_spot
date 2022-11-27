package com.dkwasniak.slr_spot_backend.criterion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CriterionTypeRepository extends JpaRepository<CriterionType, Long> {

    Optional<CriterionType> findByName(String name);
}
