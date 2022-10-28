package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.role.Role;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    void activateUser(String email);
    Role saveRole(Role role);
    User getUser(String username);
    List<User> getUsers();
    void changePassword(User user, String password);
    void updatePassword(String username, String oldPassword, String newPassword);
    void changeEmail(String oldEmail, String newEmail, String password);
    void updateEmail(String oldEmail, String newEmail);
}
