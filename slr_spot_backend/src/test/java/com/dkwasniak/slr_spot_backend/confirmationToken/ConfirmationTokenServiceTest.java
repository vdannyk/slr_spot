package com.dkwasniak.slr_spot_backend.confirmationToken;

import com.dkwasniak.slr_spot_backend.confirmationToken.exception.ConfirmationTokenExpiredException;
import com.dkwasniak.slr_spot_backend.confirmationToken.exception.ConfirmationTokenNotFoundException;
import com.dkwasniak.slr_spot_backend.confirmationToken.exception.EmailAlreadyConfirmedException;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.tag.exception.TagNotFoundException;
import com.dkwasniak.slr_spot_backend.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenServiceTest {

    @InjectMocks
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Test
    public void createConfirmationToken_shouldCreateToken() {
        var user = new User("test", "test", "test@gmail.com", "123");

        var confirmationToken = confirmationTokenService.createConfirmationToken(user);

        assertEquals("test@gmail.com", confirmationToken.getUser().getEmail());
    }

    @Test
    public void getConfirmationToken_shouldReturnToken_whenExists() {
        var user = new User("test", "test", "test@gmail.com", "123");
        var token = new ConfirmationToken("token", LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), user);

        when(confirmationTokenRepository.findByToken(token.getToken())).thenReturn(Optional.of(token));
        var confirmationToken = confirmationTokenService.getConfirmationToken(token.getToken());

        assertEquals("test@gmail.com", confirmationToken.getUser().getEmail());
    }

    @Test
    public void getConfirmationToken_shouldThrowConfirmationTokenNotFoundException_whenNotExists() {
        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        assertThrows(ConfirmationTokenNotFoundException.class,
                () -> confirmationTokenService.getConfirmationToken("token"));
    }

    @Test
    public void confirmToken_shouldReturnToken_whenExists() {
        var user = new User("test", "test", "test@gmail.com", "123");
        var token = new ConfirmationToken("token", LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), user);

        when(confirmationTokenRepository.updateConfirmedAt(anyString(), any())).thenReturn(1);
        var confirmed = confirmationTokenService.confirmToken(token);

        assertEquals(1, confirmed);
    }

    @Test
    public void confirmToken_shouldEmailAlreadyConfirmedExcception_whenEmailConfirmed() {
        var user = new User("test", "test", "test@gmail.com", "123");
        var token = new ConfirmationToken("token", LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), user);
        token.setConfirmedAt(LocalDateTime.now());

        assertThrows(EmailAlreadyConfirmedException.class,
                () -> confirmationTokenService.confirmToken(token));
    }

    @Test
    public void confirmToken_shouldThrowConfirmationTokenExpiredException_whenTokenExpired() {
        var user = new User("test", "test", "test@gmail.com", "123");
        var token = new ConfirmationToken("token", LocalDateTime.now(), LocalDateTime.now().minusMinutes(1), user);

        assertThrows(ConfirmationTokenExpiredException.class,
                () -> confirmationTokenService.confirmToken(token));
    }
}
