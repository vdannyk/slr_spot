package com.dkwasniak.slr_spot_backend.imports;

import com.dkwasniak.slr_spot_backend.deduplication.DeduplicationService;
import com.dkwasniak.slr_spot_backend.deduplication.DeduplicationType;
import com.dkwasniak.slr_spot_backend.deduplication.dto.DeduplicationDto;
import com.dkwasniak.slr_spot_backend.file.FileService;
import com.dkwasniak.slr_spot_backend.imports.exception.NothingToImportException;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.study.exception.StudyMappingException;
import com.dkwasniak.slr_spot_backend.study.reader.RisReader;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import com.dkwasniak.slr_spot_backend.user.exception.UserNotFoundException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.jbibtex.BibTeXParser;
import org.jbibtex.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ImportFacadeTest {

    private static final String CSV_EXAMPLE = "title,authors,year\n" +
            "test,test2,2012";
    private static final String BIB_EXAMPLE = "@ARTICLE{Feng20232892,\n" +
            "author={test},\n" +
            "title={title},\n" +
            "year={2023},\n" +
            "}";
    private static final String BIB_EXAMPLE_WRONG = "@ARTICLE{Feng20232892,\n" +
            "asdas={test},\n" +
            "xd={title},\n" +
            "as={2023},\n" +
            "}";


    @InjectMocks
    private ImportFacade importFacade;

    @Mock
    private ImportService importService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private ImportRepository importRepository;
    @Mock
    private DeduplicationService deduplicationService;
    @Mock
    private UserService userService;
    @Mock
    private RisReader risReader;
    @Mock
    private FileService fileService;

    @Test
    public void importStudies_shouldPerformImportFromCsv_whenFileContentCsv() throws IOException {
        var mockFile = new MockMultipartFile("test", "test", "text/csv", new byte[0]);
        var importContext = new ImportContext(
                mockFile, 1L, "SCOPUS", "search", "info", 1L, DeduplicationType.DOI
        );
        var stream = new ByteArrayInputStream(CSV_EXAMPLE.getBytes(StandardCharsets.UTF_8));
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(bufferedReader,
                CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).setTrim(true).setIgnoreHeaderCase(true).build());

        when(fileService.isCsvFile(anyString())).thenReturn(true);
        mockImport();
        when(fileService.loadFromCsv(any())).thenReturn(csvParser.getRecords());
        importFacade.importStudies(importContext);

        verify(fileService, times(1)).loadFromCsv(any());
        verify(importRepository, times(1)).save(any());
    }

    @Test
    public void importStudies_shouldPerformImportFromBib_whenFileContentBib() throws ParseException {
        var mockFile = new MockMultipartFile("test", "test", "application/octet-stream", new byte[0]);
        var importContext = new ImportContext(
                mockFile, 1L, "SCOPUS", "search", "info", 1L, DeduplicationType.DOI
        );
        var stream = new ByteArrayInputStream(BIB_EXAMPLE.getBytes(StandardCharsets.UTF_8));
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8));
        BibTeXParser bibTeXParser = new BibTeXParser();

        when(fileService.isBibtexFile(anyString())).thenReturn(true);
        mockImport();
        when(fileService.loadFromBibtex(any())).thenReturn(bibTeXParser.parse(bufferedReader));
        importFacade.importStudies(importContext);

        verify(fileService, times(1)).loadFromBibtex(any());
        verify(importRepository, times(1)).save(any());
    }

    @Test
    public void importStudies_shouldThrowNothingToImportException_whenNoValidBibRecords() throws ParseException {
        var mockFile = new MockMultipartFile("test", "test", "application/octet-stream", new byte[0]);
        var importContext = new ImportContext(
                mockFile, 1L, "SCOPUS", "search", "info", 1L, DeduplicationType.DOI
        );
        var stream = new ByteArrayInputStream(BIB_EXAMPLE_WRONG.getBytes(StandardCharsets.UTF_8));
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8));
        BibTeXParser bibTeXParser = new BibTeXParser();

        when(fileService.isBibtexFile(anyString())).thenReturn(true);
        when(fileService.loadFromBibtex(any())).thenReturn(bibTeXParser.parse(bufferedReader));

        assertThrows(NothingToImportException.class,
                () -> importFacade.importStudies(importContext));
    }

    @Test
    public void importStudies_shouldPerformImportFromRis_whenFileContentRis() throws IOException, ParseException {
        var mockFile = new MockMultipartFile("test", "test", "application/octet-stream", new byte[0]);
        var importContext = new ImportContext(
                mockFile, 1L, "SCOPUS", "search", "info", 1L, DeduplicationType.DOI
        );
        List<Map<String, String>> risResults = new ArrayList<>();
        Map<String, String> result= new HashMap<>();
        result.put("TI", "Test title");
        result.put("AU", "Test author");
        result.put("PY", "2012");
        risResults.add(result);

        when(fileService.isRisFile(anyString())).thenReturn(true);
        mockImport();
        when(risReader.read(any())).thenReturn(risResults);
        importFacade.importStudies(importContext);

        verify(risReader, times(1)).read(any());
        verify(importRepository, times(1)).save(any());
    }

    @Test
    public void importStudies_shouldThrowStudyMappingException_whenWrongRisRecords() {
        var mockFile = new MockMultipartFile("test", "test", "application/octet-stream", new byte[0]);
        var importContext = new ImportContext(
                mockFile, 1L, "SCOPUS", "search", "info", 1L, DeduplicationType.DOI
        );
        List<Map<String, String>> risResults = new ArrayList<>();
        Map<String, String> result= new HashMap<>();
        result.put("TI", "Test title");
        result.put("AU", "Test author");
        result.put("XD", "2012");
        risResults.add(result);

        when(fileService.isRisFile(anyString())).thenReturn(true);
        when(risReader.read(any())).thenReturn(risResults);

        assertThrows(StudyMappingException.class,
                () -> importFacade.importStudies(importContext));
    }

    private void mockImport() {
        var review = new Review();
        review.setNumOfRemovedDuplicates(0);
        review.setNumOfImportedStudies(0);
        var user = new User("test", "test", "test@gmail.com", "123");

        doNothing().when(fileService).checkIfAllowedFileContentType(anyString());
        when(deduplicationService.removeDuplicates(anyLong(), any(), any())).thenReturn(DeduplicationDto
                .builder()
                .correctStudies(List.of(new Study()))
                .numOfRemovedDuplicates(0)
                .build());
        when(reviewService.getReviewById(anyLong())).thenReturn(review);
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(importRepository.save(any())).thenReturn(new Import());
    }
}
