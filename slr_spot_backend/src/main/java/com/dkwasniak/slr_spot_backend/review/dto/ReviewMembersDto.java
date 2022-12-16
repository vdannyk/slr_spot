package com.dkwasniak.slr_spot_backend.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewMembersDto {

    private List<String> membersToAdd;
}
