package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.confirmationToken.ConfirmationToken;
import com.dkwasniak.slr_spot_backend.confirmationToken.ConfirmationTokenService;
import com.dkwasniak.slr_spot_backend.email.EmailService;
import com.dkwasniak.slr_spot_backend.role.Role;
import com.dkwasniak.slr_spot_backend.role.RoleRepository;
import com.dkwasniak.slr_spot_backend.user.dto.UpdatePasswordDto;
import com.dkwasniak.slr_spot_backend.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserFacade {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    public long createUser(User user) {
        User savedUser = userService.saveUser(user);

        ConfirmationToken confirmationToken = confirmationTokenService.createConfirmationToken(savedUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String activationLink = String.format("http://localhost:3000/activate/%s", confirmationToken.getToken());
        emailService.sendVerificationEmail(user.getEmail(), activationLink);
        return savedUser.getId();
    }

    @Transactional
    public void confirmAccount(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationToken(token);
        confirmationTokenService.confirmToken(confirmationToken);
//        User user = confirmationToken.getUser();
        userService.activateUser(confirmationToken.getUser().getEmail());
    }

    public void addRoleToUser(String username, String roleName) {
        log.info("Role {} added to user {}", roleName, username);
        User user = userService.getUser(username);
        Role role = roleRepository.findByName(roleName).orElseThrow();
        user.getRoles().add(role);
    }

    public void updateEmail(String oldEmail, String newEmail) {
        User user = userService.getUser(oldEmail);
        ConfirmationToken confirmationToken = confirmationTokenService.createConfirmationToken(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String activationLink = String.format("http://localhost:3000/activate/%s", confirmationToken.getToken());
        emailService.sendVerificationEmail(newEmail, activationLink);
    }

    public void updatePassword(String username, UpdatePasswordDto updatePasswordDto) {
        userService.updatePassword(username,
                updatePasswordDto.getOldPassword(),
                updatePasswordDto.getNewPassword(),
                updatePasswordDto.getConfirmPassword());
    }

//    public void updateEmail(String username, String newEmail) {
//        userService.updateEmail(username, newEmail);
//    }

    public void updateName(String username, UserDto userDto) {
        userService.updateName(username, userDto.getFirstName(), userDto.getLastName());
    }
}
