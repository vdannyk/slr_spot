package com.dkwasniak.slr_spot_backend.tag.exception;

public class TagNotFoundException extends RuntimeException {

    private static final String MESSAGE = "TAG WITH ID %s NOT FOUND";

    public TagNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
