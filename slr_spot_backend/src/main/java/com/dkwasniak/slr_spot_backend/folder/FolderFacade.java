package com.dkwasniak.slr_spot_backend.folder;

import com.dkwasniak.slr_spot_backend.folder.dto.FolderRequest;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class FolderFacade {

    private final FolderService folderService;
    private final ReviewService reviewService;

    public long addFolder(FolderRequest folderRequest) {
        Folder folder = new Folder(folderRequest.getName());
        if (!isNull(folderRequest.getParentId())) {
            folder.setParent(folderService.getFolderById(folderRequest.getParentId()));
        }
        folder.setReview(reviewService.getReviewById(folderRequest.getReviewId()));
        return folderService.saveFolder(folder);
    }

    public void removeFolderById(Long id) {
        folderService.removeFolderById(id);
    }
}
