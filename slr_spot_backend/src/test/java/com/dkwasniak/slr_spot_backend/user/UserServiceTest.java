package com.dkwasniak.slr_spot_backend.user;


import com.dkwasniak.slr_spot_backend.user.exception.UserAlreadyExistException;
import com.dkwasniak.slr_spot_backend.user.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Load user when exists in database")
    public void loadUserByUsername_shouldReturnUser_whenUserExistsInDatabaseAndIsActive() {
        var user = new User("test", "test", "test@gmail.com", "123");
        user.setIsActivated(true);
        var securityUser = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(new HashSet<>())
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        var loadedUser = userService.loadUserByUsername("test@gmail.com");
        assertEquals(securityUser, loadedUser);
    }

    @Test
    @DisplayName("Throw exception when loading not existing user")
    public void saveUser_shouldThrowUserNotFoundException_whenUserNotExistsInDatabase() {
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.loadUserByUsername("test@gmail.com"));
    }

    @Test
    @DisplayName("Throw exception when loading not confirmed user")
    public void saveUser_shouldThrowUserNotConfirmedException_whenUserExistsInDatabaseButIsNotConfirmed() {
        var user = new User("test", "test", "test@gmail.com", "123");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class,
                () -> userService.loadUserByUsername("test@gmail.com"));
    }

    @Test
    @DisplayName("Should save user")
    public void saveUser_shouldSaveUser_whenNotExistInDatabase() {
        var user = new User("test", "test", "test@gmail.com", "123");
        
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(userRepository.save(user)).thenReturn(user);
        
        var savedUser = userService.saveUser(user);
        assertEquals(user, savedUser);
    }

    @Test
    @DisplayName("Throw exception when saving already existing user")
    public void saveUser_shouldThrowUserAlreadyExistException_whenUserExistsInDatabase() {
        var user = new User("test", "test", "test@gmail.com", "123");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistException.class,
                () -> userService.saveUser(user));
    }
}
