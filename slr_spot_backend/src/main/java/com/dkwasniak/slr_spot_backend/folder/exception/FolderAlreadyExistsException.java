package com.dkwasniak.slr_spot_backend.folder.exception;

public class FolderAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE = "Folder %s already exists in review";

    public FolderAlreadyExistsException(String name) {
        super(String.format(MESSAGE, name));
    }
}
