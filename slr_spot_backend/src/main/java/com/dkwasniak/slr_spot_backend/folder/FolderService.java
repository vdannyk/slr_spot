package com.dkwasniak.slr_spot_backend.folder;

import com.dkwasniak.slr_spot_backend.folder.exception.FolderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public Folder getFolderById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new FolderNotFoundException(id));
    }

    public List<Folder> getRootFolders() {
        return folderRepository.findByParentIsNull();
    }

    public long saveFolder(Folder folder) {
        return folderRepository.save(folder).getId();
    }

    public void removeFolderById(Long id) {
        folderRepository.deleteById(id);
    }
}
