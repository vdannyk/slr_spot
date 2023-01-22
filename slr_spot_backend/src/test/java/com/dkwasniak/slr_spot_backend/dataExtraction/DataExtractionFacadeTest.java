package com.dkwasniak.slr_spot_backend.dataExtraction;

import com.dkwasniak.slr_spot_backend.dataExtraction.dto.ExtractionRequest;
import com.dkwasniak.slr_spot_backend.file.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DataExtractionFacadeTest {

    @InjectMocks
    private DataExtractionFacade dataExtractionFacade;

    @Mock
    private FileService fileService;

    @Test
    public void extractData_shouldReturnResource_whenExtractionSuccess() {
        var extractionRq = new ExtractionRequest(new ArrayList<>(), new ArrayList<>());
        var resource = new InputStreamResource(new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray()));

        when(fileService.exportCsv(any(), any(), any())).thenReturn(resource);
        var extractData = dataExtractionFacade.extractData(extractionRq);

        assertNotNull(extractData);
    }
}
