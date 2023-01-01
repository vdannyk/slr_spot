package com.dkwasniak.slr_spot_backend.folder;

import com.dkwasniak.slr_spot_backend.folder.exception.FolderNotFoundException;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.study.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final StudyService studyService;

    public List<Folder> getAllFoldersByReviewId(Long reviewId) {
        return folderRepository.findAllByReview_Id(reviewId);
    }

    public Folder getFolderById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new FolderNotFoundException(id));
    }

    public List<Folder> getRootFolders(Long reviewId) {
        return folderRepository.findByReview_IdAndParentIsNull(reviewId);
    }

    public void addStudies(Long folderId, List<Long> studies) {
        Folder folder = getFolderById(folderId);
        for (var id : studies) {
            Study study = studyService.getStudyById(id);
            folder.addStudy(study);
        }
        folderRepository.save(folder);
    }

    public long saveFolder(Folder folder) {
        return folderRepository.save(folder).getId();
    }

    public void removeFolderById(Long id) {
        folderRepository.deleteById(id);
    }
}
