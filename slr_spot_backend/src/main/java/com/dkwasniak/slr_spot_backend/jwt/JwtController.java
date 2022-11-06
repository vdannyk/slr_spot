package com.dkwasniak.slr_spot_backend.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.dkwasniak.slr_spot_backend.exception.ErrorResponse;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.generateJwt;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.getUsername;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.validateHeader;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.validateJwt;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(EndpointConstants.API_PATH + "/auth/refresh")
@RequiredArgsConstructor
public class JwtController {

    private final ObjectMapper objectMapper;
    private final UserService userService;

    @PostMapping
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        RefreshTokenRequest refreshTokenRequest =
                new ObjectMapper().readValue(request.getInputStream(), RefreshTokenRequest.class);
        if (validateHeader(authorizationHeader)) {
            try {
                String refreshToken = refreshTokenRequest.getRefreshToken();
                DecodedJWT decodedJWT = validateJwt(refreshToken);
                User user = userService.getUser(getUsername(decodedJWT));
                String jwtToken = generateJwt(user, request);
                JwtResponse jwtResponse = new JwtResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getRoles(),
                        jwtToken,
                        refreshToken
                );
                response.setContentType(APPLICATION_JSON_VALUE);
                objectMapper.writeValue(response.getOutputStream(), jwtResponse);
            } catch (Exception exception) {
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(FORBIDDEN.value());
                ErrorResponse error = new ErrorResponse(FORBIDDEN, exception.getMessage(), request.getServletPath());
                objectMapper.writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new AuthorizationHeaderException("Refresh token error");
        }
    }
}
