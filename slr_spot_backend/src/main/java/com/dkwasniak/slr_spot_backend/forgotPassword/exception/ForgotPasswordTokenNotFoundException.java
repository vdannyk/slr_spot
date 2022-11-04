package com.dkwasniak.slr_spot_backend.forgotPassword.exception;

public class ForgotPasswordTokenNotFoundException extends RuntimeException {

    public ForgotPasswordTokenNotFoundException(String message) {
        super(message);
    }
}
