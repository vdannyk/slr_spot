package com.dkwasniak.slr_spot_backend.forgotPassword;

import com.dkwasniak.slr_spot_backend.forgotPassword.exception.ForgotPasswordTokenExpiredException;
import com.dkwasniak.slr_spot_backend.forgotPassword.exception.ForgotPasswordTokenNotFoundException;
import com.dkwasniak.slr_spot_backend.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ForgotPasswordServiceTest {

    @InjectMocks
    private ForgotPasswordService forgotPasswordService;

    @Mock
    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @Test
    public void save() {
        var token = new ForgotPasswordToken("token", new User());

        when(forgotPasswordTokenRepository.save(any())).thenReturn(token);
        forgotPasswordService.save(token);

        verify(forgotPasswordTokenRepository, times(1)).save(any());
    }

    @Test
    public void getByToken_shouldReturnToken_whenExists() {
        var token = new ForgotPasswordToken("token", new User());

        when(forgotPasswordTokenRepository.findByToken(any())).thenReturn(java.util.Optional.of(token));
        var tokenFound = forgotPasswordService.getByToken("token");

        assertEquals(token.getToken(), tokenFound.getToken());
    }

    @Test
    public void generatePasswordResetToken() {
        var token = forgotPasswordService.generatePasswordResetToken();

        assertNotNull(token);
    }

    @Test
    public void getByToken_shouldThrowException_whenNotExists() {
        when(forgotPasswordTokenRepository.findByToken(any())).thenReturn(java.util.Optional.empty());

        assertThrows(ForgotPasswordTokenNotFoundException.class,
                () -> forgotPasswordService.getByToken("token"));
    }

    @Test
    public void validate_shouldThrowException_whenTokenExpired() {
        var token = new ForgotPasswordToken("token", new User());
        token.setId(1L);
        token.setExpiresAt(LocalDateTime.now().minusMinutes(10));

        assertThrows(ForgotPasswordTokenExpiredException.class,
                () -> forgotPasswordService.validate(token));
    }
}
