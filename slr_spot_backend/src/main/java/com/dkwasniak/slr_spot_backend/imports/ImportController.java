package com.dkwasniak.slr_spot_backend.imports;

import com.dkwasniak.slr_spot_backend.imports.dto.ImportDto;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointConstants.API_PATH + "/imports")
public class ImportController {

    private final ImportFacade importFacade;

    @PostMapping
    public ResponseEntity<Void> saveImport(@ModelAttribute ImportContext importContext) {
        importFacade.importStudies(importContext);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{importId}")
    public ResponseEntity<Void> deleteImport(@PathVariable Long importId) {
        importFacade.removeImportById(importId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Set<ImportDto>> getImportsByReviewId(@RequestParam Long reviewId) {
        return ResponseEntity.ok().body(importFacade.getImportsByReviewId(reviewId));
    }
}
