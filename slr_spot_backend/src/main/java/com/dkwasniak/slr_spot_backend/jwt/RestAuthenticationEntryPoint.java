package com.dkwasniak.slr_spot_backend.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.Instant.now;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String msg = "Invalid username or password";
        String errorBody = String.format("{\n" +
                "   \"status\": \"%s\",\n" +
                "   \"message\": \"%s\",\n" +
                "   \"timestamp\": \"" + LocalDateTime.ofInstant(now(), ZoneId.systemDefault()) + "\",\n" +
                "   \"path\": \"%s\"\n" +
                "}",
                response.getStatus(), msg, "/api/auth/signin/");
        try (PrintWriter out = response.getWriter()) {
            out.write(errorBody);
        }
        resolver.resolveException(request, response, null, exception);
    }
}
