package com.dkwasniak.slr_spot_backend.folder;

import com.dkwasniak.slr_spot_backend.folder.dto.FolderAssignDto;
import com.dkwasniak.slr_spot_backend.folder.dto.FolderRequest;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(EndpointConstants.API_PATH + "/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;
    private final FolderFacade folderFacade;

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping
    public ResponseEntity<List<Folder>> getFolders(@RequestParam Long reviewId) {
        return ResponseEntity.ok().body(folderService.getAllFoldersByReviewId(reviewId));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/tree")
    public ResponseEntity<List<Folder>> getFolderTree(@RequestParam Long reviewId) {
        return ResponseEntity.ok().body(folderService.getRootFolders(reviewId));
    }

    @PostAuthorize("hasScreeningAccess(#folderRequest.reviewId)")
    @PostMapping
    public ResponseEntity<Long> addFolder(@RequestBody FolderRequest folderRequest) {
        long id = folderFacade.addFolder(folderRequest);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path(EndpointConstants.API_PATH + "/folders").buildAndExpand(id).toUriString());
        return ResponseEntity.created(uri).body(id);
    }

    @PostMapping("/{id}/studies")
    public ResponseEntity<Long> addStudies(@PathVariable Long id,
                                           @RequestBody FolderAssignDto folderAssignDto) {
        folderService.addStudies(id, folderAssignDto.getStudiesId());
        return ResponseEntity.ok().build();
    }

    @PostAuthorize("hasScreeningAccess(#reviewId)")
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long folderId, @RequestParam Long reviewId) {
        folderFacade.removeFolderById(folderId);
        return ResponseEntity.ok().build();
    }

}
