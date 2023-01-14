package com.dkwasniak.slr_spot_backend.folder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByReview_IdAndParentIsNull(long reviewId);
    List<Folder> findAllByReview_Id(long reviewId);
    boolean existsByNameAndReview_Id(String name, long reviewId);
    boolean existsByNameAndReview_IdAndParent_Id(String name, long reviewId, long parentId);
}
