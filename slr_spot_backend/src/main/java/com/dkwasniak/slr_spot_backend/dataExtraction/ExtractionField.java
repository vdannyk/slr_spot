package com.dkwasniak.slr_spot_backend.dataExtraction;

public enum ExtractionField {
    AUTHORS("authors"),
    TITLE("title"),
    JOURNAL("journalTitle"),
    PUBLICATIONYEAR("publicationYear"),
    ABSTRACT("documentAbstract"),
    DOI("doi");

    private final String fieldName;

    ExtractionField(String field) {
        this.fieldName = field;
    }

    @Override
    public String toString() {
        return fieldName;
    }
}
