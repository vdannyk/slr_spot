package com.dkwasniak.slr_spot_backend.user;

import lombok.Data;

@Data
public class PasswordDto {

    private String token;
    private String oldPassword;
    private String newPassword;
}
