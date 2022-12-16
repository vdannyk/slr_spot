package com.dkwasniak.slr_spot_backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDto {

    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
