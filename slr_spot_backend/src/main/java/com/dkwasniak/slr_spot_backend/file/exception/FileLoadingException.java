package com.dkwasniak.slr_spot_backend.file.exception;

public class FileLoadingException extends RuntimeException {

    private static final String MESSAGE = "Unable to load file %s";

    public FileLoadingException(String filename) {
        super(String.format(MESSAGE, filename));
    }
}
