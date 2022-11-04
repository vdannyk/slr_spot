package com.dkwasniak.slr_spot_backend.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.dkwasniak.slr_spot_backend.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.extractJwtFromHeader;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.getAuthorities;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.getUsername;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.validateHeader;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.validateJwt;
import static com.dkwasniak.slr_spot_backend.util.EndpointConstants.API_PATH;
import static com.dkwasniak.slr_spot_backend.util.EndpointConstants.AUTH_PATH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String SIGN_IN_PATH = API_PATH + AUTH_PATH;
    private static final String REFRESH_JWT_TOKEN_PATH = API_PATH + "/users/refreshtoken";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isPathWithoutAuthorization(request.getServletPath())) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (validateHeader(authorizationHeader)) {
                try {
                    String jwtToken = extractJwtFromHeader(authorizationHeader);
                    DecodedJWT decodedJWT = validateJwt(jwtToken);

                    String username = getUsername(decodedJWT);
                    Collection<SimpleGrantedAuthority> authorities = getAuthorities(decodedJWT);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    log.error("Error in JwtAuthorizationFilter: {}", exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setStatus(UNAUTHORIZED.value());
                    ErrorResponse error = new ErrorResponse(UNAUTHORIZED, exception.getMessage(), request.getServletPath());
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    objectMapper.writeValue(response.getOutputStream(), error);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    private boolean isPathWithoutAuthorization(String path) {
        return path.equals(SIGN_IN_PATH) || path.equals(REFRESH_JWT_TOKEN_PATH);
    }
}
