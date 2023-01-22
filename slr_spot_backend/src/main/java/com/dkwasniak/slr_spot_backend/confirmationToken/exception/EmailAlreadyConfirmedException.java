package com.dkwasniak.slr_spot_backend.confirmationToken.exception;

public class EmailAlreadyConfirmedException extends RuntimeException {

    private static final String MESSAGE = "Email already confirmed";

    public EmailAlreadyConfirmedException() {
        super(MESSAGE);
    }
}
