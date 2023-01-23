package com.dkwasniak.slr_spot_backend.study.writer;

import com.dkwasniak.slr_spot_backend.study.Study;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class CsvWriter {

    private final static String[] DEFAULT_CSV_HEADERS = {"title", "authors", "journal title", "publication year", "volume", "doi", "url", "abstract", "issn", "language"};

    public InputStreamResource write(List<Study> studies) {
        CSVFormat format = CSVFormat.Builder.create().setHeader(DEFAULT_CSV_HEADERS).build();
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new PrintWriter(outputStream), format);
        ) {
            for (var study : studies) {
                printer.printRecord(study.getTitle(), study.getAuthors(), study.getJournalTitle(), study.getPublicationYear(),
                        study.getVolume(), study.getDoi(), study.getUrl(), study.getDocumentAbstract(), study.getIssn(), study.getLanguage());
            }
            printer.flush();

            return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            throw new IllegalStateException("Error while exporting studies to csv");
        }
    }

}
