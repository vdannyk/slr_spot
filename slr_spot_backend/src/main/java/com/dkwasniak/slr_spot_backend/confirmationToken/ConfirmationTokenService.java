package com.dkwasniak.slr_spot_backend.confirmationToken;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public ConfirmationToken getConfirmationToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
    }

    public int confirmToken(ConfirmationToken token) {
        if (checkIfTokenConfirmed(token)) {
            throw new IllegalStateException("Email already confirmed");
        }

        if (checkIfTokenExpired(token)) {
            throw new IllegalStateException("Token expired");
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
