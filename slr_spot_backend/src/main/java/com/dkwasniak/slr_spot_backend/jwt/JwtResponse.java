package com.dkwasniak.slr_spot_backend.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String accessToken;
    private String refreshToken;
}
