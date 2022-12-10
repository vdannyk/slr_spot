package com.dkwasniak.slr_spot_backend.folder;

import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(EndpointConstants.API_PATH + "/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @GetMapping
    public ResponseEntity<List<Folder>> getFolderTree() {
        return ResponseEntity.ok().body(folderService.getRootFolders());
    }
}
