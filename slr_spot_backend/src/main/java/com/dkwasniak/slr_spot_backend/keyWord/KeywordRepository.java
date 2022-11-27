package com.dkwasniak.slr_spot_backend.keyWord;

import com.dkwasniak.slr_spot_backend.criterion.CriterionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<KeyWord, Long> {

    boolean existsByNameAndType_Name(String name, String type);
    Optional<KeyWord> findByNameAndType(String name, CriterionType type);
    Optional<KeyWord> findByIdAndType(long id, CriterionType type);
}
