package com.dkwasniak.slr_spot_backend.file;

import com.dkwasniak.slr_spot_backend.study.Study;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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

//    public List<CSVRecord> loadFromFile(MultipartFile multipartFile) {
//        if (isNotAllowedContentType(multipartFile.getContentType())) {
//            throw new IllegalStateException("Not allowed file");
//        }
//
//        List<CSVRecord> records = loadFromCsv(multipartFile);;
////        switch (multipartFile.getContentType()) {
////            case "text/bib":
////                records = loadFromBibtex(multipartFile);
////            case "text/ris":
////                records = loadFromRis(multipartFile);
////            default:
////                records =
////        }
//        return records;
//    }

     public List<CSVRecord> loadFromCsv(MultipartFile multipartFile) {
        try (
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8)
            );
            CSVParser csvParser = new CSVParser(bufferedReader,
                CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).setTrim(true).build())
        ) {
            return csvParser.getRecords();
//            List<Study> studies = new ArrayList<>();
//
//            for (CSVRecord csvRecord : csvParser.getRecords()) {
//                Study study = new Study(csvRecord.get("Document Title"));
//                studies.add(study);
//            }
//            return studies;
        } catch (IOException e) {
            throw new IllegalStateException("fail to parse csv");
        }
    }

    private List<CSVRecord> loadFromBibtex(MultipartFile multipartFile) {
        return null;
    }

    private List<CSVRecord> loadFromRis(MultipartFile multipartFile) {
        return null;
    }
}
