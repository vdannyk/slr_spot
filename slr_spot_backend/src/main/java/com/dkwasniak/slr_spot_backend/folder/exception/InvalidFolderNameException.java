package com.dkwasniak.slr_spot_backend.folder.exception;

public class InvalidFolderNameException extends RuntimeException {

    private static final String MESSAGE = "Invalid folder name";

    public InvalidFolderNameException() {
        super(MESSAGE);
    }
}
