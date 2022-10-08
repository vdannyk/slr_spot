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
}
