package com.dkwasniak.slr_spot_backend.exception;

import com.dkwasniak.slr_spot_backend.imports.excception.NothingToImportException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ImportExceptionHandler extends CommonExceptionHandler {

    @ExceptionHandler(NothingToImportException.class)
    ResponseEntity<ErrorResponse> handleNothingToImportException(NothingToImportException ex, HttpServletRequest req) {
        return createResponse(req, ex, HttpStatus.BAD_REQUEST);
    }

}
