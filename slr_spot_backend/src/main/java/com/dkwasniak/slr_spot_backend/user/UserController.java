package com.dkwasniak.slr_spot_backend.user;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.dkwasniak.slr_spot_backend.exception.ErrorResponse;
import com.dkwasniak.slr_spot_backend.jwt.AuthorizationHeaderException;
import com.dkwasniak.slr_spot_backend.jwt.JwtResponse;
import com.dkwasniak.slr_spot_backend.jwt.RefreshTokenRequest;
import com.dkwasniak.slr_spot_backend.user.dto.EmailUpdateDto;
import com.dkwasniak.slr_spot_backend.user.dto.PasswordDto;
import com.dkwasniak.slr_spot_backend.user.dto.PersonalInformationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.generateJwt;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.getUsername;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.validateHeader;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.validateJwt;


@Controller
@RequestMapping(path = "api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserFacade userFacade;
    private final ObjectMapper objectMapper;

    @PostMapping("/save")
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        long id = userFacade.createUser(user);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{id}").buildAndExpand(id).toUriString());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmAccount(@RequestParam String confirmationToken) {
        userFacade.confirmAccount(confirmationToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refreshtoken")
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

    @PostMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestParam String email) {
        userFacade.resetPassword(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verifyResetPassword")
    public ResponseEntity<String> verifyResetPassword(@RequestParam String resetToken) throws Exception {
        userFacade.validateResetPasswordToken(resetToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/savePassword")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordDto passwordDto) throws Exception {
        User user = userFacade.getUserByPasswordResetToken(passwordDto.getToken());
        userService.changePassword(user, passwordDto.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRq) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userFacade.updatePassword(username, updatePasswordRq.getOldPassword(), updatePasswordRq.getNewPassword(),
                updatePasswordRq.getConfirmPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<String> changeEmail(@RequestBody EmailUpdateDto emailUpdateDto) throws Exception {
        userFacade.changeEmail(emailUpdateDto.getNewEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateEmail")
    public ResponseEntity<String> updateEmail(@RequestBody EmailUpdateDto emailUpdateDto) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userFacade.updateEmail(username, emailUpdateDto.getNewEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updatePersonal")
    public ResponseEntity<String> updateEmail(@RequestBody PersonalInformationDto personalInfoDto) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userFacade.updatePersonalInformation(username, personalInfoDto);
        return ResponseEntity.ok().build();
    }
}
