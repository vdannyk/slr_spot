package com.dkwasniak.slr_spot_backend.criterion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CriterionRepository extends JpaRepository<Criterion, Long> {

    boolean existsByNameAndType_Name(String name, String type);
    Optional<Criterion> findByNameAndType(String name, CriterionType type);
    Optional<Criterion> findByIdAndType(long id, CriterionType type);

}
