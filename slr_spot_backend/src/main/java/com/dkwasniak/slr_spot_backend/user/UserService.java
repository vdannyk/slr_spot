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
}
