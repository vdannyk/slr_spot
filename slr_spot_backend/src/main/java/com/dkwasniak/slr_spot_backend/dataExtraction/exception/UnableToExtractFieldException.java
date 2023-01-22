package com.dkwasniak.slr_spot_backend.dataExtraction.exception;

public class UnableToExtractFieldException extends RuntimeException {

    private static final String MESSAGE = "Unable to extract field %s";

    public UnableToExtractFieldException(String field) {
        super(String.format(MESSAGE, field));
    }
}
