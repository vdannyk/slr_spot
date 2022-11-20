package com.dkwasniak.slr_spot_backend.review.dto;

import com.dkwasniak.slr_spot_backend.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ReviewDto {

    private String firstName;
    private String lastName;
    private Review review;
}
