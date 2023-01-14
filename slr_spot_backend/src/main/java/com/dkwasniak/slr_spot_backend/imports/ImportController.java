package com.dkwasniak.slr_spot_backend.imports;

import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointConstants.API_PATH + "/imports")
public class ImportController {

    private final ImportFacade importFacade;

    @PostAuthorize("hasScreeningAccess(#importContext.reviewId)")
    @PostMapping
    public ResponseEntity<Void> saveImport(@ModelAttribute ImportContext importContext) {
        importFacade.importStudies(importContext);
        return ResponseEntity.ok().build();
    }

    @PostAuthorize("hasScreeningAccess(#reviewId)")
    @DeleteMapping("/{importId}")
    public ResponseEntity<Void> deleteImport(@PathVariable Long importId, @RequestParam Long reviewId) {
        importFacade.removeImportById(importId);
        return ResponseEntity.ok().build();
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping
    public ResponseEntity<Page<Import>> getImportsByReviewId(@RequestParam Long reviewId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(importFacade.getImportsByReviewId(reviewId, page, size));
    }
}
