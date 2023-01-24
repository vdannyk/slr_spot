package com.dkwasniak.slr_spot_backend.file;

import com.dkwasniak.slr_spot_backend.dataExtraction.ExtractionField;
import com.dkwasniak.slr_spot_backend.file.exception.FileLoadingException;
import com.dkwasniak.slr_spot_backend.file.exception.NotAllowedFileContentTypeException;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.user.exception.UserAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    private static final String CSV_EXAMPLE = "title,authors,publication year\n" +
            "test,test2,2012";
    private static final String BIB_EXAMPLE = "@ARTICLE{Feng20232892,\n" +
            "author={test},\n" +
            "title={title},\n" +
            "year={2023},\n" +
            "}";

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

    @Test
    public void csvToStudies_shouldReturnCsvRecords() throws IOException {
        var stream = new ByteArrayInputStream(CSV_EXAMPLE.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", stream);

        var res = fileService.loadFromCsv(mockMultipartFile);

        assertEquals(1, res.size());
    }

    @Test
    public void csvToStudies_shouldThrowException_whenInvalidFile() {
        MockMultipartFile mockMultipartFile =
                new ExceptionMockMultipartFile("test", "ttest", "test", new byte[0]);

        assertThrows(FileLoadingException.class,
                () -> fileService.loadFromCsv(mockMultipartFile));
    }

    @Test
    public void bibToStudies() throws IOException {
        var stream = new ByteArrayInputStream(BIB_EXAMPLE.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", stream);

        var res = fileService.loadFromBibtex(mockMultipartFile);

        assertEquals(1, res.getObjects().size());
    }

    @Test
    public void exportCsv() throws IOException {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test2").doi("1234").build();
        List<Study> studies = List.of(study1, study2);
        List<ExtractionField> extractionFields = List.of(ExtractionField.TITLE, ExtractionField.DOI);
        String[] headers = new String[] {"title", "doi"};

        var res = fileService.exportCsv(extractionFields, headers, studies);

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (res.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        assertEquals("title,doitest1,123test2,1234", textBuilder.toString().replaceAll("\r\n", ""));
    }

    @Test
    public void write_shouldWriteInCsvFormat_whenCsvFormatPassed() throws IOException {
        var study1 = Study.builder().title("test1").doi("123").build();
        var study2 = Study.builder().title("test2").doi("1234").build();
        List<Study> studies = List.of(study1, study2);

        var res = fileService.write(studies, "CSV");

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (res.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        assertEquals("title,authors,journal title,publication year,volume,doi,url,abstract,issn,languagetest1,,,,,123,,,,test2,,,,,1234,,,,",
                textBuilder.toString().replaceAll("\r\n", ""));
    }

    @Test
    public void write_shouldWriteInBibFormat_whenBibFormatPassed() throws IOException {
        var study1 = Study.builder().id(1L).title("test1").doi("123").build();
        var study2 = Study.builder().id(2L).title("test2").doi("1234").build();
        List<Study> studies = List.of(study1, study2);
        var expectedResult = "@article{1,\n" +
                "\ttitle = {test1},\n" +
                "\tyear = {null},\n" +
                "\tdoi = {123}\n" +
                "}\n" +
                "\n" +
                "@article{2,\n" +
                "\ttitle = {test2},\n" +
                "\tyear = {null},\n" +
                "\tdoi = {1234}\n" +
                "}";

        var res = fileService.write(studies, "BIB");

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (res.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        assertEquals(expectedResult,textBuilder.toString());
    }

    private class ExceptionMockMultipartFile extends MockMultipartFile {

        public ExceptionMockMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            super(name, originalFilename, contentType, content);
        }

        //Method is overrided, so that it throws an IOException, when it's called
        @Override
        public InputStream getInputStream() throws IOException {
            throw new IOException();
        }
    }
}
