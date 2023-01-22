package com.dkwasniak.slr_spot_backend.confirmationToken;

import com.dkwasniak.slr_spot_backend.confirmationToken.exception.ConfirmationTokenExpiredException;
import com.dkwasniak.slr_spot_backend.confirmationToken.exception.ConfirmationTokenNotFoundException;
import com.dkwasniak.slr_spot_backend.confirmationToken.exception.EmailAlreadyConfirmedException;
import com.dkwasniak.slr_spot_backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken createConfirmationToken(User user) {
        String token = UUID.randomUUID().toString();
        return new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public ConfirmationToken getConfirmationToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(ConfirmationTokenNotFoundException::new);
    }

    public int confirmToken(ConfirmationToken token) {
        if (checkIfTokenConfirmed(token)) {
            throw new EmailAlreadyConfirmedException();
        }

        if (checkIfTokenExpired(token)) {
            throw new ConfirmationTokenExpiredException(token.getExpiresAt().toString());
        }
        return confirmationTokenRepository.updateConfirmedAt(token.getToken(), LocalDateTime.now());
    }

    private boolean checkIfTokenConfirmed(ConfirmationToken token) {
        return nonNull(token.getConfirmedAt());
    }

    private boolean checkIfTokenExpired(ConfirmationToken token) {
        return token.getExpiresAt().isBefore(LocalDateTime.now());
    }
}
