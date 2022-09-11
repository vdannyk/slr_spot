package com.dkwasniak.slr_spot_backend.user;

import lombok.Data;

@Data
public class RoleToUserRequest {

    private String username;
    private String roleName;
}
