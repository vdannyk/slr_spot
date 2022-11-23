package com.dkwasniak.slr_spot_backend.review.dto;

import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewMemberDto {

    private long memberId;
    private String firstName;
    private String lastName;
    private String email;
    private ReviewRole role;
}
