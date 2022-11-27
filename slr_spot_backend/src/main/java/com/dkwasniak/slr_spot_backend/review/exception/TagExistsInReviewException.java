package com.dkwasniak.slr_spot_backend.review.exception;

public class TagExistsInReviewException extends RuntimeException {

    public TagExistsInReviewException(String message) {
        super(message);
    }
}
