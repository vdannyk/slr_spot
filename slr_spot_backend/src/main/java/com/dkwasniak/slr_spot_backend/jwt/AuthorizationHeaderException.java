package com.dkwasniak.slr_spot_backend.jwt;

public class AuthorizationHeaderException extends RuntimeException {

    public AuthorizationHeaderException(String message) {
        super(message);
    }
}
