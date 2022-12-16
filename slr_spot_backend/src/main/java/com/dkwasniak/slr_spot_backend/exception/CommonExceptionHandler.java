package com.dkwasniak.slr_spot_backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

@Getter
public class CommonExceptionHandler {

    protected ResponseEntity<ErrorResponse> createResponse(HttpServletRequest req, Exception ex, HttpStatus httpStatus) {
        String msg = ex.getMessage();
        var path = req.getServletPath();
        return new ResponseEntity<>(new ErrorResponse(httpStatus, msg, path), httpStatus);
    }
}
