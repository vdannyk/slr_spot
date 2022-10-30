package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.role.Role;
import com.dkwasniak.slr_spot_backend.role.RoleRepository;
import com.dkwasniak.slr_spot_backend.user.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DefaultUserService implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optUser = userRepository.findByEmail(username);

        if (optUser.isEmpty()) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        if (!optUser.get().getIsActivated()) {
            log.error("User account is not activated");
            throw new IllegalStateException("User " + username + " not activated");
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
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User already exists");
        }
        log.info("Saving new user: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role: {}", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void activateUser(String email) {
        userRepository.enableUser(email);
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
    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword, String confirmPassword) {
        User user = getUser(username);
        if (!checkIfCorrectPassword(user, oldPassword)) {
            throw new IllegalStateException("Invalid old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void changeEmail(String username, String oldPassword, String newPassword) {
        User user = getUser(username);
        if (!checkIfCorrectPassword(user, oldPassword)) {
            throw new IllegalStateException("Invalid old password");
        }
    }

    @Override
    public void updateEmail(String oldEmail, String newEmail) {
        User user = getUser(oldEmail);
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    private boolean checkIfCorrectPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

}
