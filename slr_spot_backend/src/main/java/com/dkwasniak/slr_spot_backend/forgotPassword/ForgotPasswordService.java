package com.dkwasniak.slr_spot_backend.forgotPassword;

import com.dkwasniak.slr_spot_backend.forgotPassword.exception.ForgotPasswordTokenExpiredException;
import com.dkwasniak.slr_spot_backend.forgotPassword.exception.ForgotPasswordTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    public void save(ForgotPasswordToken forgotPasswordToken) {
        forgotPasswordTokenRepository.save(forgotPasswordToken);
    }

    public String generatePasswordResetToken() {
        return UUID.randomUUID().toString();
    }

    public ForgotPasswordToken getByToken(String token) {
        return forgotPasswordTokenRepository.findByToken(token)
                .orElseThrow(() -> new ForgotPasswordTokenNotFoundException("Forgot password token not found"));
    }

    public void validate(ForgotPasswordToken token) {
        if (isResetPasswordTokenExpired(token)) {
            throw new ForgotPasswordTokenExpiredException("Forgot password token expired");
        }
    }

    private boolean isResetPasswordTokenExpired(ForgotPasswordToken token) {
        return token.getExpiresAt().isBefore(LocalDateTime.now());
    }
}
