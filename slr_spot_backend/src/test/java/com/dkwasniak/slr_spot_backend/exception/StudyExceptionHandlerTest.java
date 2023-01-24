package com.dkwasniak.slr_spot_backend.exception;

import com.dkwasniak.slr_spot_backend.study.exception.StudyMappingException;
import com.dkwasniak.slr_spot_backend.study.exception.StudyMappingInvalidHeadersException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StudyExceptionHandlerTest {

    @InjectMocks
    private StudyExceptionHandler studyExceptionHandler;

    @Test
    public void handleStudyMappingException_shouldReturnBadRequestStatus() {
        var httpServletRq = new MockHttpServletRequest();
        httpServletRq.setServletPath("testPath");
        var exception = new StudyMappingException("test", new IllegalStateException());

        var response = studyExceptionHandler.handleStudyMappingException(
                exception, httpServletRq
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("testPath", Objects.requireNonNull(response.getBody()).getPath());
        assertEquals(String.format("Unable to map studies from file %s", "test"), response.getBody().getMessage());
    }

    @Test
    public void handleStudyMappingInvalidHeadersException_shouldReturnBadRequestStatus() {
        var httpServletRq = new MockHttpServletRequest();
        httpServletRq.setServletPath("testPath");
        var exception = new StudyMappingInvalidHeadersException("test", new IllegalStateException());

        var response = studyExceptionHandler.handleStudyMappingException(
                exception, httpServletRq
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("testPath", Objects.requireNonNull(response.getBody()).getPath());
        assertEquals("Unable to map studies, invalid headers", response.getBody().getMessage());
    }

    @Test
    public void handleFullTextTooBigException_shouldReturnPayloadTooLargeStatus() {
        var httpServletRq = new MockHttpServletRequest();
        httpServletRq.setServletPath("testPath");
        var exception = new SizeLimitExceededException("test", 1L, 1L);

        var response = studyExceptionHandler.handleFullTextTooBigException(
                exception, httpServletRq
        );

        assertEquals(HttpStatus.PAYLOAD_TOO_LARGE, response.getStatusCode());
        assertEquals("testPath", Objects.requireNonNull(response.getBody()).getPath());
        assertEquals("Too big file size", response.getBody().getMessage());
    }

    @Test
    public void handleNotSelectedException_shouldReturnBadRequestStatus() {
        var httpServletRq = new MockHttpServletRequest();
        httpServletRq.setServletPath("testPath");
        var exception = new MissingServletRequestPartException("test");

        var response = studyExceptionHandler.handleNotSelectedException(
                exception, httpServletRq
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("testPath", Objects.requireNonNull(response.getBody()).getPath());
        assertEquals("No file selected", response.getBody().getMessage());
    }
}
