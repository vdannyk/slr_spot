package com.dkwasniak.slr_spot_backend.forgotPassword;

import com.dkwasniak.slr_spot_backend.email.EmailService;
import com.dkwasniak.slr_spot_backend.forgotPassword.dto.ForgotPasswordDto;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ForgotPasswordFacadeTest {

    @InjectMocks
    private ForgotPasswordFacade forgotPasswordFacade;

    @Mock
    private ForgotPasswordService forgotPasswordService;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;

    @Test
    public void resetPassword_shouldCreateResetToken() {
        var user = new User();
        user.setEmail("test@gmail.com");
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(forgotPasswordService.generatePasswordResetToken()).thenReturn("token");
        doNothing().when(forgotPasswordService).save(any());
        doNothing().when(emailService).sendResetPasswordEmail(anyString(), anyString());

        forgotPasswordFacade.resetPassword("test@gmail.com");

        verify(emailService, times(1)).sendResetPasswordEmail(anyString(), anyString());
    }

    @Test
    public void validateToken() {
        var token = new ForgotPasswordToken("token", new User());
        when(forgotPasswordService.getByToken(anyString())).thenReturn(token);

        forgotPasswordFacade.validateToken("token");

        verify(forgotPasswordService, times(1)).validate(token);
    }

    @Test
    public void resetPassword() {
        var token = new ForgotPasswordToken("token", new User());
        var forgotPasswordDto = new ForgotPasswordDto();
        forgotPasswordDto.setToken("token");
        forgotPasswordDto.setNewPassword("newPassword");

        when(forgotPasswordService.getByToken(anyString())).thenReturn(token);
        doNothing().when(userService).updatePassword(any(), anyString());
        forgotPasswordFacade.resetPassword(forgotPasswordDto);

        verify(userService, times(1)).updatePassword(any(), anyString());
    }
}
