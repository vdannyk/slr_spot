package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.confirmationToken.ConfirmationToken;
import com.dkwasniak.slr_spot_backend.confirmationToken.ConfirmationTokenService;
import com.dkwasniak.slr_spot_backend.email.EmailService;
import com.dkwasniak.slr_spot_backend.role.Role;
import com.dkwasniak.slr_spot_backend.role.RoleRepository;
import com.dkwasniak.slr_spot_backend.user.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserFacade {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public String saveUser(User user) {
        User savedUser = userService.saveUser(user);

        // confirmation token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                savedUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String activationLink = String.format("http://localhost:3000/activate/%s", token);
        emailService.sendVerificationEmail(user.getEmail(), activationLink);
        return token;
    }

    @Transactional
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getConfirmationToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        if (!isNull(confirmationToken.getConfirmedAt())) {
            throw new IllegalStateException("Email already confirmed");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        userService.activateUser(confirmationToken.getUser().getEmail());
    }

    public void createPasswordResetToken(User user, String token) {
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(resetToken);
    }

    public void constructResetTokenEmail(String token, User user) {
        String resetPasswordLink =  String.format("http://localhost:3000/password-recovery/%s", token);
        emailService.sendResetPasswordEmail(user.getEmail(), resetPasswordLink);
    }

    public void validateResetPasswordToken(String token) throws Exception {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken.isEmpty())
            throw new Exception("Reset token not found");
        if (isResetPasswordTokenExpired(passwordResetToken.get()))
            throw new Exception("Reset token expired");
    }

    private boolean isResetPasswordTokenExpired(PasswordResetToken token) {
        return token.getExpiresAt().isBefore(LocalDateTime.now());
    }

    public User getUserByPasswordResetToken(String token) {
        Optional<PasswordResetToken> resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken.isEmpty())
            return null;
        return resetToken.get().getUser();
    }

    public void addRoleToUser(String username, String roleName) {
        log.info("Role {} added to user {}", roleName, username);
        User user = userService.getUser(username);
        Role role = roleRepository.findByName(roleName).orElseThrow();
        user.getRoles().add(role);
    }
}
