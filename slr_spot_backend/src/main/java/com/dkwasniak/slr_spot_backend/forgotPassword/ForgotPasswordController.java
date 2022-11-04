package com.dkwasniak.slr_spot_backend.forgotPassword;

import com.dkwasniak.slr_spot_backend.forgotPassword.dto.EmailDto;
import com.dkwasniak.slr_spot_backend.forgotPassword.dto.ForgotPasswordDto;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EndpointConstants.API_PATH + "/password")
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordController {

    private final ForgotPasswordFacade forgotPasswordFacade;

    @PostMapping(path = "/forgot")
    public ResponseEntity<Void> resetPassword(@RequestBody EmailDto email) {
        forgotPasswordFacade.resetPassword(email.getEmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/forgot")
    public ResponseEntity<String> verifyResetPassword(@RequestParam String resetToken) {
        forgotPasswordFacade.validateToken(resetToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) throws Exception {
        forgotPasswordFacade.resetPassword(forgotPasswordDto);
        return ResponseEntity.ok().build();
    }

}
