package com.dkwasniak.slr_spot_backend.jwt;

import com.dkwasniak.slr_spot_backend.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private Collection<Role> roles;
    private String accessToken;
    private String refreshToken;
}
