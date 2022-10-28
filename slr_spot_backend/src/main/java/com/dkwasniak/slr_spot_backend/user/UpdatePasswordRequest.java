package com.dkwasniak.slr_spot_backend.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePasswordRequest {

    private final String username;
    private final String oldPassword;
    private final String newPassword;
//    private final String confirmPassword;
}
