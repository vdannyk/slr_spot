package com.dkwasniak.slr_spot_backend.folder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public List<Folder> getRootFolders() {
        return folderRepository.findByParentIsNull();
    }
}
