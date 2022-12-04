package com.dkwasniak.slr_spot_backend.study.mapper;

import com.dkwasniak.slr_spot_backend.study.Study;
import org.apache.commons.csv.CSVRecord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvToStudyMapper {

    private static Map<String, String[]> HEADERS = new HashMap<>() {{
        put("SCOPUS", new String[]{"Authors", "Title", "Year", "Source title", "Volume", "Abstract",
        "ISSN", "Link"});
    }};

    public static List<Study> csvToStudies(List<CSVRecord> csvRecords, String source) {
        List<Study> studies = new ArrayList<>();
        String[] headers = HEADERS.get(source);

        for (CSVRecord csvRecord : csvRecords) {
            Study study = Study.builder()
                    .authors(csvRecord.get("\uFEFFAuthors"))
                    .documentTitle(csvRecord.get(headers[1]))
                    .publicationYear(Integer.valueOf(csvRecord.get(headers[2])))
                    .publicationTitle(csvRecord.get(headers[3]))
                    .volume(csvRecord.get(headers[4]))
                    .documentAbstract(csvRecord.get(headers[5]))
                    .issn(csvRecord.get(headers[6]))
                    .url(csvRecord.get(headers[7]))
                    .build();
            studies.add(study);
        }
        return studies;
    }

    public static List<Study> bibToStudies(List<CSVRecord> csvRecords, String source) {
        List<Study> studies = new ArrayList<>();
        String[] headers = HEADERS.get(source);

        for (CSVRecord csvRecord : csvRecords) {
            Study study = Study.builder()
                    .authors(csvRecord.get("\uFEFFAuthors"))
                    .documentTitle(csvRecord.get(headers[1]))
                    .publicationYear(Integer.valueOf(csvRecord.get(headers[2])))
                    .publicationTitle(csvRecord.get(headers[3]))
                    .volume(csvRecord.get(headers[4]))
                    .documentAbstract(csvRecord.get(headers[5]))
                    .issn(csvRecord.get(headers[6]))
                    .url(csvRecord.get(headers[7]))
                    .build();
            studies.add(study);
        }
        return studies;
    }
}
