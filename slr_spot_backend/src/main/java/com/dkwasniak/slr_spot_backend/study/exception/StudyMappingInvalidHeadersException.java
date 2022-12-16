package com.dkwasniak.slr_spot_backend.study.exception;

public class StudyMappingInvalidHeadersException extends RuntimeException {

    private static final String MESSAGE = "Unable to map studies, invalid headers";

    public StudyMappingInvalidHeadersException(String message, Throwable cause) {
        super(MESSAGE, cause);
    }
}
