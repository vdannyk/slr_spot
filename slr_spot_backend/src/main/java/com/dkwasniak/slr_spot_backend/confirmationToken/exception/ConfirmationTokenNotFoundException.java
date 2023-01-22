package com.dkwasniak.slr_spot_backend.confirmationToken.exception;

public class ConfirmationTokenNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Confirmation token not found";

    public ConfirmationTokenNotFoundException() {
        super(MESSAGE);
    }
}
