package com.dkwasniak.slr_spot_backend.file.exception;

public class NotAllowedFileContentTypeException extends RuntimeException {

    private static final String MESSAGE = "File with content-type %s not allowed";

    public NotAllowedFileContentTypeException(String contentType) {
        super(String.format(MESSAGE, contentType));
    }
}
