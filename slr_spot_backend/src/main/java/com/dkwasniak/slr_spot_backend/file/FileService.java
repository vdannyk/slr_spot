package com.dkwasniak.slr_spot_backend.file;

import com.dkwasniak.slr_spot_backend.study.Study;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private final static List<String> ALLOWED_CONTENT = List.of("text/csv", "text/bib");

    public boolean isNotAllowedContentType(String contentType) {
        return !ALLOWED_CONTENT.contains(contentType);
    }

     public List<CSVRecord> loadFromCsv(MultipartFile multipartFile) {
        try (
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8)
            );
            CSVParser csvParser = new CSVParser(bufferedReader,
                CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).setTrim(true).build())
        ) {
            return csvParser.getRecords();
        } catch (IOException e) {
            throw new IllegalStateException("fail to parse csv");
        }
    }

    public BibTeXDatabase loadFromBibtex(MultipartFile multipartFile) {
        try (
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8)
            )
        ) {
            BibTeXParser bibTeXParser = new BibTeXParser();
            return bibTeXParser.parse(bufferedReader);
        } catch (IOException | ParseException e) {
            throw new IllegalStateException("fail to parse bib");
        }
    }

    private List<CSVRecord> loadFromRis(MultipartFile multipartFile) {
        return null;
    }
}
