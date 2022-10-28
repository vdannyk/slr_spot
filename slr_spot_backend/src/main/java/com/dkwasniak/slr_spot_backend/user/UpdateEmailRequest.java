package com.dkwasniak.slr_spot_backend.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateEmailRequest {

    private final String oldEmail;
    private final String newEmail;
    private final String password;
}
