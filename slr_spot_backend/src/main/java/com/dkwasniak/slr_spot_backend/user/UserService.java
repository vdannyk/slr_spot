package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.user.dto.UserDto;
import com.dkwasniak.slr_spot_backend.user.exception.UserAlreadyExistException;
import com.dkwasniak.slr_spot_backend.user.exception.UserNotFoundException;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        Optional<User> optUser = userRepository.findByEmail(username);

        if (optUser.isEmpty()) {
            log.error("User not found in the database");
            throw new UserNotFoundException("User " + username + " not found");
        }
        if (!optUser.get().getIsActivated()) {
            log.error("User account is not activated");
            throw new IllegalStateException("User " + username + " not activated");
        }
        User user = optUser.get();
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(new HashSet<>())
                .build();
    }

    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User already exists");
        }
        log.info("Saving new user: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void activateUser(String email) {
        log.info("Activating user: {}", email);
        userRepository.enableUser(email);
    }

    public User getUserByEmail(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User " + id + " not found"));
    }

    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void updatePassword(String username, String oldPassword, String newPassword, String confirmPassword) {
        User user = getUserByEmail(username);
        if (!checkIfCorrectPassword(user, oldPassword)) {
            throw new IllegalStateException("Invalid old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void updateEmail(User user, String newEmail) {
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public void updateName(String username, String firstName, String lastName) {
        User user = getUserByEmail(username);
        if (firstName != null && !firstName.isEmpty()) {
            user.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            user.setLastName(lastName);
        }
        userRepository.save(user);
    }

    private boolean checkIfCorrectPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public Set<String> getAllEmails() {
        return userRepository.getEmails();
    }

    public UserDto toUserDto(UserReview userReview) {
        return UserDto.builder()
                .userId(userReview.getUser().getId())
                .firstName(userReview.getUser().getFirstName())
                .lastName(userReview.getUser().getLastName())
                .email(userReview.getUser().getEmail())
                .role(userReview.getRole().getName())
                .build();
    }

}
