package com.dkwasniak.slr_spot_backend.forgotPassword.exception;

public class ForgotPasswordTokenExpiredException extends RuntimeException {

    public ForgotPasswordTokenExpiredException(String message) {
        super(message);
    }
}
