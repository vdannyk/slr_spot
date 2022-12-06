package com.dkwasniak.slr_spot_backend.file;

import com.dkwasniak.slr_spot_backend.file.exception.FileLoadingException;
import com.dkwasniak.slr_spot_backend.file.exception.NotAllowedFileContentTypeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXParser;
import org.jbibtex.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class FileService {

    private final static String CSV_MEDIA_TYPE = "text/csv";
    private final static String BIB_MEDIA_TYPE = "application/octet-stream";
    private final static String RIS_MEDIA_TYPE = "application/x-research-info-systems";
    private final static String XLS_MEDIA_TYPE = "xls";
    private final static List<String> ALLOWED_CONTENT = List.of(
            CSV_MEDIA_TYPE, BIB_MEDIA_TYPE, RIS_MEDIA_TYPE, XLS_MEDIA_TYPE
    );

    public void checkIfAllowedFileContentType(String contentType) {
        if  (!ALLOWED_CONTENT.contains(contentType)) {
            log.error(String.format("File with content-type %s not allowed", contentType));
            throw new NotAllowedFileContentTypeException(contentType);
        }
    }

    public boolean isCsvFile(String contentType) {
        return CSV_MEDIA_TYPE.equals(contentType);
    }

    public boolean isBibtexFile(String contentType) {
        return BIB_MEDIA_TYPE.equals(contentType);
    }

    public boolean isRisFile(String contentType) {
        return RIS_MEDIA_TYPE.equals(contentType);
    }

    public boolean isXlsFile(String contentType) {
        return XLS_MEDIA_TYPE.equals(contentType);
    }

     public List<CSVRecord> loadFromCsv(MultipartFile multipartFile) {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8))) {

            CSVParser csvParser = new CSVParser(bufferedReader,
                    CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).setTrim(true).setIgnoreHeaderCase(true).build());

            return csvParser.getRecords();
        } catch (IOException e) {
            log.error("Error while parsing csv file", e);
            throw new FileLoadingException(multipartFile.getName());
        }
    }

    public BibTeXDatabase loadFromBibtex(MultipartFile multipartFile) {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8))) {

            BibTeXParser bibTeXParser = new BibTeXParser();

            return bibTeXParser.parse(bufferedReader);
        } catch (IOException | ParseException e) {
            log.error("Error while parsing bib file", e);
            throw new FileLoadingException(multipartFile.getName());
        }
    }

    private List<CSVRecord> loadFromRis(MultipartFile multipartFile) {
        return null;
    }
}
