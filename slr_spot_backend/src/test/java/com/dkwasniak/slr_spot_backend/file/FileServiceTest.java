package com.dkwasniak.slr_spot_backend.file;

import com.dkwasniak.slr_spot_backend.file.exception.NotAllowedFileContentTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Test
    public void checkIfAllowedFileContentType_shouldThrowException_whenContentTypeNotAllowed() {
        assertThrows(NotAllowedFileContentTypeException.class,
                () -> fileService.checkIfAllowedFileContentType("test"));
    }

    @Test
    public void isCsvFile_shouldReturnTrue_whenCsv() {
        assertTrue(fileService.isCsvFile("text/csv"));
    }

    @Test
    public void isRisFile_shouldReturnTrue_whenRis() {
        assertTrue(fileService.isRisFile("application/x-research-info-systems"));
    }

    @Test
    public void isBibFile_shouldReturnTrue_whenBib() {
        assertTrue(fileService.isBibtexFile("application/octet-stream"));
    }

    @Test
    public void isPdfFile_shouldReturnTrue_whenPdf() {
        assertTrue(fileService.isPdfFile("application/pdf"));
    }

}
