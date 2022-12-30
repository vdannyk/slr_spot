package com.dkwasniak.slr_spot_backend.dataExtraction;

import com.dkwasniak.slr_spot_backend.dataExtraction.dto.ExtractionRequest;
import com.dkwasniak.slr_spot_backend.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class DataExtractionFacade {

    private final FileService fileService;

    public InputStreamResource extractData(ExtractionRequest extractionRequest) {
        List<ExtractionField> extractionFields = extractionRequest.getFields();
        String[] headers = extractionFields.stream().map(ExtractionField::toString).toArray(String[]::new);
        return fileService.exportCsv(extractionFields, headers, extractionRequest.getStudies());
    }
}
