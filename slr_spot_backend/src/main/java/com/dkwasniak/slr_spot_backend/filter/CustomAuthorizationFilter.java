package com.dkwasniak.slr_spot_backend.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.getAuthorities;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.getUsername;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.validateJwt;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private static final String SIGN_IN_PATH = "/api/auth/signin";
    private static final String REFRESH_JWT_TOKEN_PATH = "/api/token/refresh";
    private static final String AUTHORIZATION_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isPathWithoutAuthorization(request.getServletPath())) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (!StringUtils.isEmpty(authorizationHeader) && authorizationHeader.startsWith(AUTHORIZATION_PREFIX)) {
                try {
                    String jwtToken = authorizationHeader.substring(AUTHORIZATION_PREFIX.length());
                    DecodedJWT decodedJWT = validateJwt(jwtToken);

                    String username = getUsername(decodedJWT);
                    Collection<SimpleGrantedAuthority> authorities = getAuthorities(decodedJWT);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    log.error("Error while logging: {}", exception.getMessage());
                    response.setHeader("error", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>() {{
                        put("error_msg", exception.getMessage());
                    }};
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
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
