package com.dkwasniak.slr_spot_backend.user.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
