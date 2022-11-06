package com.dkwasniak.slr_spot_backend.forgotPassword;

import com.dkwasniak.slr_spot_backend.email.EmailService;
import com.dkwasniak.slr_spot_backend.forgotPassword.dto.ForgotPasswordDto;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class ForgotPasswordFacade {

    private final ForgotPasswordService forgotPasswordService;
    private final UserService userService;
    private final EmailService emailService;

    public void resetPassword(String email) {
        User user = userService.getUser(email);
        String token = forgotPasswordService.generatePasswordResetToken();
        forgotPasswordService.save(new ForgotPasswordToken(token, user));
        constructResetTokenEmail(token, user);
    }

    public void constructResetTokenEmail(String token, User user) {
        String resetPasswordLink = String.format("http://localhost:3000/password-recovery/%s", token);
        emailService.sendResetPasswordEmail(user.getEmail(), resetPasswordLink);
    }

    public void validateToken(String token) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.getByToken(token);
        forgotPasswordService.validate(forgotPasswordToken);
    }

    public void resetPassword(ForgotPasswordDto forgotPasswordDto) {
        User user = forgotPasswordService.getByToken(forgotPasswordDto.getToken()).getUser();
        userService.updatePassword(user, forgotPasswordDto.getNewPassword());
    }
}
