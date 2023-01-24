package com.dkwasniak.slr_spot_backend.userReview.exception;

public class RoleChangeNotAllowedException extends RuntimeException {

    private final static String MESSAGE = "ROLE CHANGE NOT ALLOWED";

    public RoleChangeNotAllowedException() {
        super(MESSAGE);
    }
}
