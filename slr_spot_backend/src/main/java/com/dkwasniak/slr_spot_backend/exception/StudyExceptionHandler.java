package com.dkwasniak.slr_spot_backend.exception;

import com.dkwasniak.slr_spot_backend.study.exception.StudyMappingException;
import com.dkwasniak.slr_spot_backend.study.exception.StudyMappingInvalidHeadersException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class StudyExceptionHandler extends CommonExceptionHandler {

    @ExceptionHandler(StudyMappingException.class)
    ResponseEntity<ErrorResponse> handleStudyMappingException(StudyMappingException ex, HttpServletRequest req) {
        return createResponse(req, ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudyMappingInvalidHeadersException.class)
    ResponseEntity<ErrorResponse> handleStudyMappingException(StudyMappingInvalidHeadersException ex, HttpServletRequest req) {
        return createResponse(req, ex, HttpStatus.BAD_REQUEST);
    }
}
