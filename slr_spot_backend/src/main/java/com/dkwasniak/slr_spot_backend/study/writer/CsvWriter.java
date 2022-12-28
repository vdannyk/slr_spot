package com.dkwasniak.slr_spot_backend.study.writer;

import com.dkwasniak.slr_spot_backend.dataExtraction.DataExtractionUtils;
import com.dkwasniak.slr_spot_backend.dataExtraction.ExtractionField;
import com.dkwasniak.slr_spot_backend.study.Study;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;


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

    public InputStreamResource write(List<ExtractionField> extractionFields, String[] headers, List<Study> studies) {
        CSVFormat format = CSVFormat.Builder.create().setHeader(headers).build();
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new PrintWriter(outputStream), format);
        ) {
            for (var study : studies) {
                printer.printRecord(extractionFields.stream().map(e -> DataExtractionUtils.getField(study, e)).collect(Collectors.toList()));
            }
            printer.flush();

            return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            throw new IllegalStateException("Error while extracting data");
        }
    }

}
