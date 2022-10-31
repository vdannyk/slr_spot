package com.dkwasniak.slr_spot_backend.user;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.dkwasniak.slr_spot_backend.jwt.JwtResponse;
import com.dkwasniak.slr_spot_backend.jwt.RefreshTokenRequest;
import com.dkwasniak.slr_spot_backend.role.RoleToUserRequest;
import com.dkwasniak.slr_spot_backend.user.dto.EmailUpdateDto;
import com.dkwasniak.slr_spot_backend.user.dto.PasswordDto;
import com.dkwasniak.slr_spot_backend.user.dto.PersonalInformationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.generateJwt;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static com.dkwasniak.slr_spot_backend.jwt.JwtUtils.validateJwt;


@Controller
@RequestMapping(path = "api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserFacade userFacade;

    @PostMapping("/users/save")
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        long id = userFacade.createUser(user);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{id}").buildAndExpand(id).toUriString());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/users/confirm")
    public ResponseEntity<Void> confirmAccount(@RequestParam String confirmationToken) {
        userFacade.confirmAccount(confirmationToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/refreshtoken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        RefreshTokenRequest refreshTokenRequest =
                new ObjectMapper().readValue(request.getInputStream(), RefreshTokenRequest.class);
        if (!StringUtils.isEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = refreshTokenRequest.getRefreshToken();
                DecodedJWT decodedJWT = validateJwt(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
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
                new ObjectMapper().writeValue(response.getOutputStream(), jwtResponse);
            } catch (Exception exception) {
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(FORBIDDEN.value());
                Map<String, Object> error = new HashMap<>();
                error.put("status", FORBIDDEN.value());
                error.put("message", exception.getMessage());
//                error.put("timestamp", LocalDateTime.ofInstant(now(), ZoneId.systemDefault()));
                error.put("path", request.getServletPath());
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refreshing token error");
        }
    }

    @PostMapping("/user/resetpassword")
    public ResponseEntity<String> resetPassword(@RequestParam String email) throws Exception {
        User user = userService.getUser(email);
        if (isNull(user)) {
            throw new Exception("User not found exception");
        }

        String token = UUID.randomUUID().toString();
        userFacade.createPasswordResetToken(user, token);
        userFacade.constructResetTokenEmail(token, user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/changepassword")
    public ResponseEntity<String> changePassword(@RequestParam String resetToken) throws Exception {
        userFacade.validateResetPasswordToken(resetToken);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/savePassword")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordDto passwordDto) throws Exception {
        User user = userFacade.getUserByPasswordResetToken(passwordDto.getToken());
        userService.changePassword(user, passwordDto.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRq) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userFacade.updatePassword(username, updatePasswordRq.getOldPassword(), updatePasswordRq.getNewPassword(),
                updatePasswordRq.getConfirmPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/changeEmail")
    public ResponseEntity<String> changeEmail(@RequestBody EmailUpdateDto emailUpdateDto) throws Exception {
        userFacade.changeEmail(emailUpdateDto.getNewEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/updateEmail")
    public ResponseEntity<String> updateEmail(@RequestBody EmailUpdateDto emailUpdateDto) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userFacade.updateEmail(username, emailUpdateDto.getNewEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/updatePersonal")
    public ResponseEntity<String> updateEmail(@RequestBody PersonalInformationDto personalInfoDto) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userFacade.updatePersonalInformation(username, personalInfoDto);
        return ResponseEntity.ok().build();
    }
}
