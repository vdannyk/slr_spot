package com.dkwasniak.slr_spot_backend.exception;

import com.dkwasniak.slr_spot_backend.file.exception.FileLoadingException;
import com.dkwasniak.slr_spot_backend.file.exception.NotAllowedFileContentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class FileExceptionHandler extends CommonExceptionHandler {

    @ExceptionHandler(NotAllowedFileContentTypeException.class)
    ResponseEntity<ErrorResponse> handleNotAllowedContentTypeException(NotAllowedFileContentTypeException ex, HttpServletRequest req) {
        return createResponse(req, ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileLoadingException.class)
    ResponseEntity<ErrorResponse> handleFileLoadingException(FileLoadingException ex, HttpServletRequest req) {
        return createResponse(req, ex, HttpStatus.BAD_REQUEST);
    }
}
