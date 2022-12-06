package com.dkwasniak.slr_spot_backend.study.mapper;

import com.dkwasniak.slr_spot_backend.study.Study;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class StudyMapper {

    private static final Map<String, String[]> CSV_HEADERS = new HashMap<>() {{
        put("DEFAULT", new String[]{"Authors", "Title", "Year", "Source title", "Volume", "Abstract",
                "ISSN", "Link"});
        put("SCOPUS", new String[]{"Authors", "Title", "Year", "Source title", "Volume", "Abstract",
                "ISSN", "Link"});
        put("IEEE", new String[]{"Authors", "Document Title", "Publication Year", "Publication Title", "Volume",
                "Abstract", "ISSN"});
    }};

    public static List<Study> csvToStudies(List<CSVRecord> csvRecords, String source) {
        List<Study> studies = new ArrayList<>();
        String[] headers = CSV_HEADERS.get(source);

        for (CSVRecord csvRecord : csvRecords) {
            Study study = Study.builder()
                    .authors(csvRecord.get(headers[0]))
                    .documentTitle(csvRecord.get(headers[1]))
                    .publicationYear(Integer.valueOf(csvRecord.get(headers[2])))
                    .publicationTitle(csvRecord.get(headers[3]))
                    .volume(csvRecord.get(headers[4]))
                    .documentAbstract(csvRecord.get(headers[5]))
                    .issn(csvRecord.get(headers[6]))
//                    .url(csvRecord.get(headers[7]))
                    .build();
            studies.add(study);
        }
        return studies;
    }

    public static List<Study> bibToStudies(BibTeXDatabase bibRecords) {
        List<Study> studies = new ArrayList<>();
        Map<Key, BibTeXEntry> entries = bibRecords.getEntries();
        for (var entry : entries.entrySet()) {
            BibTeXEntry fields = entry.getValue();
            Study study = Study.builder()
                    .authors(fields.getField(new Key("author")).toUserString())
                    .documentTitle(fields.getField(new Key("title")).toUserString())
                    .publicationYear(Integer.valueOf(fields.getField(new Key("year")).toUserString()))
                    // TODO check if correct
                    .publicationTitle(fields.getField(new Key("journal")).toUserString())
                    .volume(fields.getField(new Key("volume")).toUserString())
                    .documentAbstract(fields.getField(new Key("abstract")).toUserString())
                    .url(fields.getField(new Key("url")).toUserString())
                    .build();
            studies.add(study);
        }

        return studies;
    }
}
