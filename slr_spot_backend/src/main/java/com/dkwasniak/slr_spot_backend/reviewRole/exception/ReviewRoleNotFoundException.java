package com.dkwasniak.slr_spot_backend.reviewRole.exception;

public class ReviewRoleNotFoundException extends RuntimeException {

    private final static String MESSAGE = "Role %s not found";

    public ReviewRoleNotFoundException(String roleName) {
        super(String.format(MESSAGE, roleName));
    }
}
