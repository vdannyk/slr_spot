package com.dkwasniak.slr_spot_backend.keyWord;

import com.dkwasniak.slr_spot_backend.criterion.CriterionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface KeywordRepository extends JpaRepository<KeyWord, Long> {

    boolean existsByNameAndType(String name, CriterionType type);
    Optional<KeyWord> findByNameAndType(String name, CriterionType type);
    Optional<KeyWord> findByIdAndType(long id, CriterionType type);
    Set<KeyWord> findByReview_IdAndUserNull(long reviewId);
    Set<KeyWord> findByReview_IdAndUser_Id(long reviewId, long userId);

}
