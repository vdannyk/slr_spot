package com.dkwasniak.slr_spot_backend.role;

import lombok.Data;

@Data
public class RoleToUserRequest {

    private String username;
    private String roleName;
}
