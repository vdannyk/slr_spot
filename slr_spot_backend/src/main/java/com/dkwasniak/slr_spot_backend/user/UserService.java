package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.role.Role;

import java.util.List;

public interface UserService {
    String saveUser(User user);
    void confirmToken(String token);
    void addRoleToUser(String username, String roleName);
    Role saveRole(Role role);
    User getUser(String username);
    List<User> getUsers();
    void createPasswordResetToken(User user, String token);
    void constructResetTokenEmail(String token, User user);
    void validateResetPasswordToken(String token) throws Exception;
    User getUserByPasswordResetToken(String token);
    void changePassword(User user, String password);
}
