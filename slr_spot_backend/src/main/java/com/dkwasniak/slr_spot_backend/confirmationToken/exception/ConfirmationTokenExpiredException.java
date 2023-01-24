package com.dkwasniak.slr_spot_backend.confirmationToken.exception;

public class ConfirmationTokenExpiredException extends RuntimeException {

    private static final String MESSAGE = "Confirmation token expired at %s";

    public ConfirmationTokenExpiredException(String expiredAt) {
        super(String.format(MESSAGE, expiredAt));
    }
}
