package com.dkwasniak.slr_spot_backend.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewMemberDto {

    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
