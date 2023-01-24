package com.dkwasniak.slr_spot_backend.exception;

import com.dkwasniak.slr_spot_backend.imports.Import;
import com.dkwasniak.slr_spot_backend.imports.ImportRepository;
import com.dkwasniak.slr_spot_backend.imports.ImportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CommonExceptionHandlerTest {

    @InjectMocks
    private CommonExceptionHandler commonExceptionHandler;

    @Test
    public void createResponse_shouldCreateResponseEntity_whenExceptionThrown() {
        var httpServletRq = new MockHttpServletRequest();
        httpServletRq.setServletPath("testPath");
        var exception = new IllegalStateException("testMessage");

        var response = commonExceptionHandler.createResponse(httpServletRq, exception,
                HttpStatus.BAD_REQUEST);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("testPath", Objects.requireNonNull(response.getBody()).getPath());
        assertEquals("testMessage", response.getBody().getMessage());
    }

    @Test
    public void createResponseWithMessage_shouldCreateResponseEntity_whenExceptionThrown() {
        var httpServletRq = new MockHttpServletRequest();
        httpServletRq.setServletPath("testPath");
        var exception = new IllegalStateException();

        var response = commonExceptionHandler.createResponseWithMessage(httpServletRq, exception,
                HttpStatus.BAD_REQUEST, "testMessage");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("testPath", Objects.requireNonNull(response.getBody()).getPath());
        assertEquals("testMessage", response.getBody().getMessage());
    }
}
