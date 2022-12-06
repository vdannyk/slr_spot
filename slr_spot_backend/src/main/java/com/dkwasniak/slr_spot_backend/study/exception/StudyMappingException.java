package com.dkwasniak.slr_spot_backend.study.exception;

public class StudyMappingException extends RuntimeException {

    private static final String MESSAGE = "Unable to map studies from file %s";

    public StudyMappingException(String filename, Throwable cause) {
        super(String.format(MESSAGE, filename), cause);
    }
}
