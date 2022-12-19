package com.dkwasniak.slr_spot_backend.dataExtraction;

import com.dkwasniak.slr_spot_backend.dataExtraction.dto.ExtractionRequest;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointConstants.API_PATH + "/data_extraction")
public class DataExtractionController {

    private final DataExtractionFacade dataExtractionFacade;

    @PostMapping
    public ResponseEntity<Resource> extractData(@RequestBody ExtractionRequest extractionRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "data.csv");
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(dataExtractionFacade.extractData(extractionRequest));
    }
}
