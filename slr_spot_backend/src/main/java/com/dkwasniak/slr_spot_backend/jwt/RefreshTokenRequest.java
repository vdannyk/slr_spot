package com.dkwasniak.slr_spot_backend.jwt;

import lombok.Data;

@Data
public class RefreshTokenRequest {

    private String refreshToken;
}
