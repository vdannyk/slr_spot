package com.dkwasniak.slr_spot_backend.keyWord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface KeyWordRepository extends JpaRepository<KeyWord, Long> {

    Set<KeyWord> findByReview_IdAndUser_Id(long reviewId, long userId);
    Set<KeyWord> findByReview_IdAndUserNull(long reviewId);


}
