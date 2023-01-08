package com.dkwasniak.slr_spot_backend.file.exception;

public class NotAllowedFileContentTypeException extends RuntimeException {

    private static final String CUSTOM_MESSAGE = "File with content-type %s not allowed";
    private static final String MESSAGE = "Not supported content type";

    public NotAllowedFileContentTypeException() {
        super(MESSAGE);
    }

    public NotAllowedFileContentTypeException(String contentType) {
        super(String.format(CUSTOM_MESSAGE, contentType));
    }
}
