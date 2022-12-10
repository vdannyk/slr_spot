package com.dkwasniak.slr_spot_backend.folder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByParentIsNull();
}
