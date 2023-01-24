package com.dkwasniak.slr_spot_backend.dataExtraction;

import com.dkwasniak.slr_spot_backend.dataExtraction.exception.UnableToExtractFieldException;
import com.dkwasniak.slr_spot_backend.study.Study;

public class DataExtractionUtils {

    public static String getField(Study study, ExtractionField field) {
        switch (field) {
            case AUTHORS -> {
                return study.getAuthors();
            }
            case TITLE -> {
                return study.getTitle();
            }
            case JOURNAL -> {
                return study.getJournalTitle();
            }
            case ABSTRACT -> {
                return study.getDocumentAbstract();
            }
            case PUBLICATIONYEAR -> {
                return String.valueOf(study.getPublicationYear());
            }
            case DOI -> {
                return study.getDoi();
            }
            default -> {
                throw new UnableToExtractFieldException(field.toString());
            }
        }
    }
}
