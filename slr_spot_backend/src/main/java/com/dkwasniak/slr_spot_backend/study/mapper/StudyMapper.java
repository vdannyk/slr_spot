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

import static java.util.Objects.isNull;

@Slf4j
public class StudyMapper {

    private final static String[] DEFAULT_CSV_HEADERS = {"title", "authors", "journal title", "publication year", "volume", "doi", "url", "abstract", "issn", "language"};
    private final static String[] DEFAULT_BIB_HEADERS = {"title", "author", "journal", "year", "volume", "doi", "url", "abstract", "issn", "language"};
    private final static String[] DEFAULT_RIS_HEADERS = {"TI", "AU", "T2", "PY", "VL", "DO", "UR", "AB", "SN", "LA"};
    // TI - title, AU - author, T2 - Secondary title (journal title), PY - publication year, VL - volume number
    // DO - DOI (For identification), UR - url, AB - abstract, SN - ISSN/ISBN, LA - Language

    private final static String[] SCOPUS_CSV_HEADERS = {"Title", "Authors", "Source title", "Year", "Volume", "DOI", "Link", "Abstract", "ISSN", "Language of Original Document"};

    private final static String[] IEEE_CSV_HEADERS = {"Document Title", "Authors", "Publication Title", "Publication Year", "Volume", "DOI", null, "Abstract", "ISSN", null};

    // Item Title, Publication Title, Book Series Title, Journal Volume, Journal Issue, Item DOI, Authors, Publication Year, URL, Content Type
    private final static String[] SPRINGER_CSV_HEADERS = {"Item Title", "Authors", "Publication Title", "Publication Year", "Journal Volume", "Item DOI", "URL", null, null, null};

    private final static String[] WOS_CSV_HEADERS = {"Article Title", "Authors", "Source Title", "Publication Year", "Volume", "DOI", "DOI Link", "Abstract", "ISSN", "Language"};

    private static final Map<String, String[]> CSV_HEADERS = new HashMap<>() {{
        put("SCOPUS", SCOPUS_CSV_HEADERS);
        put("IEEE", IEEE_CSV_HEADERS);
        put("SPRINGERLINK", SPRINGER_CSV_HEADERS);
    }};

    public static List<Study> csvToStudies(List<CSVRecord> csvRecords, String source) {
        List<Study> studies = new ArrayList<>();
        String[] headers = DEFAULT_CSV_HEADERS;
        if (CSV_HEADERS.containsKey(source)) {
            headers = CSV_HEADERS.get(source);
        }

        for (CSVRecord csvRecord : csvRecords) {
            String authors;
            if ("SCOPUS".equals(source)) {
                authors = csvRecord.get(0);
            } else {
                authors = tryGetElement(headers[1], csvRecord);
            }
            var publicationYear = tryGetElement(headers[3], csvRecord);
            Study study = Study.builder()
                    .title(tryGetElement(headers[0], csvRecord))
                    .authors(authors)
                    .journalTitle(tryGetElement(headers[2], csvRecord))
                    .publicationYear(publicationYear == null ? null : Integer.valueOf(publicationYear))
                    .volume(tryGetElement(headers[4], csvRecord))
                    .doi(tryGetElement(headers[5], csvRecord))
                    .url(tryGetElement(headers[6], csvRecord))
                    .documentAbstract(tryGetElement(headers[7], csvRecord))
                    .issn(tryGetElement(headers[8], csvRecord))
                    .language(tryGetElement(headers[9], csvRecord))
                    .build();
            studies.add(study);
        }
        return studies;
    }

    private static String tryGetElement(String header, CSVRecord csvRecord) {
        if (isNull(header)) {
            return null;
        }
        try {
            return csvRecord.get(header);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Study> bibToStudies(BibTeXDatabase bibRecords) {
        List<Study> studies = new ArrayList<>();
        String[] headers = DEFAULT_BIB_HEADERS;

        Map<Key, BibTeXEntry> entries = bibRecords.getEntries();
        for (var entry : entries.entrySet()) {
            BibTeXEntry fields = entry.getValue();
            Study study = Study.builder()
                    .title(isNull(fields.getField(new Key(headers[0]))) ? null : fields.getField(new Key(headers[0])).toUserString())
                    .authors(isNull(fields.getField(new Key(headers[1]))) ? null : fields.getField(new Key(headers[1])).toUserString())
                    .journalTitle(isNull(fields.getField(new Key(headers[2]))) ? null : fields.getField(new Key(headers[2])).toUserString())
                    .publicationYear(isNull(fields.getField(new Key(headers[3]))) ? null : Integer.valueOf(fields.getField(new Key(headers[3])).toUserString()))
                    .volume(isNull(fields.getField(new Key(headers[4]))) ? null : fields.getField(new Key(headers[4])).toUserString())
                    .doi(isNull(fields.getField(new Key(headers[5]))) ? null : fields.getField(new Key(headers[5])).toUserString())
                    .url(isNull(fields.getField(new Key(headers[6]))) ? null : fields.getField(new Key(headers[6])).toUserString())
                    .documentAbstract(isNull(fields.getField(new Key(headers[7]))) ? null : fields.getField(new Key(headers[7])).toUserString())
                    .issn(isNull(fields.getField(new Key(headers[8]))) ? null : fields.getField(new Key(headers[8])).toUserString())
                    .language(isNull(fields.getField(new Key(headers[9]))) ? null : fields.getField(new Key(headers[9])).toUserString())
                    .build();

            if (!hasMissingRequiredFields(study)) {
                studies.add(study);
            }
        }
        return studies;
    }

    public static List<Study> risToStudies(List<Map<String, String>> risReferences) {
        List<Study> studies = new ArrayList<>();
        String[] headers = DEFAULT_RIS_HEADERS;

        for (var reference : risReferences) {
            Study study = Study.builder()
                    .title(reference.get(headers[0]))
                    .authors(reference.get(headers[1]))
                    .journalTitle(reference.get(headers[2]))
                    .publicationYear(Integer.valueOf(reference.get(headers[3])))
                    .volume(reference.get(headers[4]))
                    .doi(reference.get(headers[5]))
                    .url(reference.get(headers[6]))
                    .documentAbstract(reference.get(headers[7]))
                    .issn(reference.get(headers[8]))
                    .language(reference.get(headers[9]))
                    .build();

            if (!hasMissingRequiredFields(study)) {
                studies.add(study);
            }
        }
        return studies;
    }

    private static boolean hasMissingRequiredFields(Study study) {
        return isNull(study.getTitle()) ||
                isNull(study.getAuthors()) ||
                isNull(study.getPublicationYear());
    }
}
