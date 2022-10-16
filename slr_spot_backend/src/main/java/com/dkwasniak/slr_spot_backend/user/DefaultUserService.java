package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.email.EmailSender;
import com.dkwasniak.slr_spot_backend.confirmationToken.ConfirmationToken;
import com.dkwasniak.slr_spot_backend.confirmationToken.ConfirmationTokenService;
import com.dkwasniak.slr_spot_backend.role.Role;
import com.dkwasniak.slr_spot_backend.role.RoleRepository;
import com.dkwasniak.slr_spot_backend.user.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DefaultUserService implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    // need to be refactored (facade)
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optUser = userRepository.findByEmail(username);

        if (optUser.isEmpty()) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        User user = optUser.get();
        Collection<GrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName())) ;
        });
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    @Override
    public String saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User already exists");
        }
        log.info("Saving new user: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // confirmation token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                savedUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String activationLink =  String.format("http://localhost:3000/activate/%s", token);
        emailSender.send(user.getEmail(), String.format(
                "Click to activate: <a href=%s>Activate Now</a>", activationLink));
        return token;
    }

    @Override
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
        this.enableUser(confirmationToken.getUser().getEmail());
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role: {}", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Role {} added to user {}", roleName, username);
        User user = userRepository.findByEmail(username).orElseThrow();
        Role role = roleRepository.findByName(roleName).orElseThrow();
        user.getRoles().add(role);
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByEmail(username).orElseThrow();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void createPasswordResetToken(User user, String token) {
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public void constructResetTokenEmail(String token, User user) {
        String resetPasswordLink =  String.format("http://localhost:3000/password-recovery/%s", token);
        emailSender.send(user.getEmail(), String.format("RESET PASSWORD HERE: <a href=%s>RESET/a>", resetPasswordLink));
    }

    @Override
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

    @Override
    public User getUserByPasswordResetToken(String token) {
        Optional<PasswordResetToken> resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken.isEmpty())
            return null;
        return resetToken.get().getUser();
    }

    @Override
    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

}
