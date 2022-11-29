package com.dkwasniak.slr_spot_backend.study;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvToStudyMapper {

    public static String TYPE = "text/csv";
    static String[] HEADERS = { "Document Title", "Authors"};

    public static boolean isCsvFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Study> csvToStudies(InputStream inputStream) {
        try (
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            CSVParser csvParser = new CSVParser(bufferedReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());)
        {
            List<Study> studies = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Study study = new Study(csvRecord.get("Document Title"));
                studies.add(study);
            }
            return studies;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse csv");
        }
    }
}
