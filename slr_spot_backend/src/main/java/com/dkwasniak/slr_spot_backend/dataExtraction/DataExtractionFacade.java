package com.dkwasniak.slr_spot_backend.dataExtraction;

import com.dkwasniak.slr_spot_backend.dataExtraction.dto.ExtractionRequest;
import com.dkwasniak.slr_spot_backend.file.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

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
