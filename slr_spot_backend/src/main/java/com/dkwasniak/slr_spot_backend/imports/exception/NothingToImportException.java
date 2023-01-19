package com.dkwasniak.slr_spot_backend.imports.exception;

public class NothingToImportException extends RuntimeException {

    private static final String MESSAGE = "No valid studies to import";

    public NothingToImportException() {
        super(MESSAGE);
    }
}
