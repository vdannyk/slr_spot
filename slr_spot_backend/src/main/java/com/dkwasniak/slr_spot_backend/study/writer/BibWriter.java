package com.dkwasniak.slr_spot_backend.study.writer;

import com.dkwasniak.slr_spot_backend.study.Study;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.Key;
import org.jbibtex.StringValue;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class BibWriter {

    private final static String[] DEFAULT_BIB_HEADERS = {"title", "author", "journal", "year", "volume", "doi", "url", "abstract", "issn", "language"};

    public InputStreamResource write(List<Study> studies) {
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ) {
            BibTeXDatabase database = new BibTeXDatabase();

            for (var study : studies) {
                BibTeXEntry entry = new BibTeXEntry(BibTeXEntry.TYPE_ARTICLE, new Key(String.valueOf(study.getId())));
                addFieldIfNotNull(entry, new Key("title"), study.getTitle());
                addFieldIfNotNull(entry, new Key("author"), study.getAuthors());
                addFieldIfNotNull(entry, new Key("journal"), study.getJournalTitle());
                addFieldIfNotNull(entry, new Key("year"), String.valueOf(study.getPublicationYear()));
                addFieldIfNotNull(entry, new Key("volume"), study.getVolume());
                addFieldIfNotNull(entry, new Key("doi"), study.getDoi());
                addFieldIfNotNull(entry, new Key("url"), study.getUrl());
                addFieldIfNotNull(entry, new Key("abstract"), study.getDocumentAbstract());
                addFieldIfNotNull(entry, new Key("issn"), study.getIssn());
                addFieldIfNotNull(entry, new Key("language"), study.getLanguage());
                database.addObject(entry);
            }

            BibTeXFormatter formatter = new BibTeXFormatter();
            formatter.format(database, new PrintWriter(outputStream));
            return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            throw new IllegalStateException("Error while exporting studies to csv");
        }
    }

    private void addFieldIfNotNull(BibTeXEntry entry, Key key, String value) {
        if (value != null) {
            entry.addField(key, new StringValue(value, StringValue.Style.BRACED));
        }
    }
}
