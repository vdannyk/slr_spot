package com.dkwasniak.slr_spot_backend.forgotPassword.dto;

import lombok.Data;

@Data
public class ForgotPasswordDto {

    private String token;
    private String newPassword;
}
