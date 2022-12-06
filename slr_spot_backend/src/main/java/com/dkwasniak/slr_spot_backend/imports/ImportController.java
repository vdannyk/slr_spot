package com.dkwasniak.slr_spot_backend.imports;

import com.dkwasniak.slr_spot_backend.study.StudyFacade;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointConstants.API_PATH + "/imports")
public class ImportController {

    private final ImportFacade importFacade;

    @PostMapping
    public ResponseEntity<Void> saveImport(@RequestParam("file") MultipartFile file,
                                           @RequestParam("reviewId") Long reviewId,
                                           @RequestParam("source") String source,
                                           @RequestParam("searchValue") String searchValue,
                                           @RequestParam("additionalInformations") String additionalInfo) {
        importFacade.importStudies(file, reviewId, source, searchValue, additionalInfo);
        return ResponseEntity.ok().build();
    }
}
