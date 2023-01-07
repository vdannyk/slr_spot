package com.dkwasniak.slr_spot_backend.exception;

import com.dkwasniak.slr_spot_backend.file.exception.NotAllowedFileContentTypeException;
import com.dkwasniak.slr_spot_backend.study.exception.StudyMappingException;
import com.dkwasniak.slr_spot_backend.study.exception.StudyMappingInvalidHeadersException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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

    @ExceptionHandler(SizeLimitExceededException.class)
    ResponseEntity<ErrorResponse> handleFullTextTooBigException(SizeLimitExceededException ex, HttpServletRequest req) {
        return createResponseWithMessage(req, ex, HttpStatus.PAYLOAD_TOO_LARGE, "Too big file size");
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    ResponseEntity<ErrorResponse> handleNotSelectedException(MissingServletRequestPartException ex, HttpServletRequest req) {
        return createResponseWithMessage(req, ex, HttpStatus.BAD_REQUEST, "No file selected");
    }

    @ExceptionHandler(NotAllowedFileContentTypeException.class)
    ResponseEntity<ErrorResponse> handleInvalidFormatException(NotAllowedFileContentTypeException ex, HttpServletRequest req) {
        return createResponse(req, ex, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
