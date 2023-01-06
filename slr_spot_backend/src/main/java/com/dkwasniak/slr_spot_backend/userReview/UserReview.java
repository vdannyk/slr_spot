package com.dkwasniak.slr_spot_backend.userReview;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users_reviews")
public class UserReview implements Serializable {

    @EmbeddedId
    private UserReviewId id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId("userId")
    private User user;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId("reviewId")
    @JsonIgnore
    private Review review;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private ReviewRole role;

    public UserReview(User user, Review review, ReviewRole reviewRole) {
        this.id = new UserReviewId(user.getId(), review.getId());
        this.user = user;
        this.review = review;
        this.role = reviewRole;
    }
}
