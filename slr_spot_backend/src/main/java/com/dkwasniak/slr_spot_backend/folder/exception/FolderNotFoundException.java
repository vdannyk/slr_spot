package com.dkwasniak.slr_spot_backend.folder.exception;

public class FolderNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Folder with id '%s' not found";

    public FolderNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
