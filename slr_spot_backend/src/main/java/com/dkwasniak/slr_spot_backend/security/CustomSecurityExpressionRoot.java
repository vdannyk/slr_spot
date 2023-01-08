package com.dkwasniak.slr_spot_backend.security;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleEnum;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.Set;

public class CustomSecurityExpressionRoot
        extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private static final Set<ReviewRoleEnum> fullAccessRoles = Set.of(ReviewRoleEnum.OWNER, ReviewRoleEnum.COOWNER);
    private static final Set<ReviewRoleEnum> screeningAccessRoles = Set.of(ReviewRoleEnum.OWNER, ReviewRoleEnum.COOWNER,
            ReviewRoleEnum.MEMBER);

    private Object filterObject;
    private Object returnObject;
    private final ReviewService reviewService;

    public CustomSecurityExpressionRoot(Authentication authentication, ReviewService reviewService) {
        super(authentication);
        this.reviewService = reviewService;
    }

    public boolean hasFullAccess(Long reviewId) {
        Map<Long, ReviewRoleEnum> userReviewsRoles = ((UserPrincipal) this.getPrincipal()).getUserReviewsRoles();
        ReviewRoleEnum currentRole = userReviewsRoles.get(reviewId);
        if (currentRole == null) {
            return false;
        }
        return fullAccessRoles.contains(currentRole);
    }

    public boolean hasScreeningAccess(Long reviewId) {
        Map<Long, ReviewRoleEnum> userReviewsRoles = ((UserPrincipal) this.getPrincipal()).getUserReviewsRoles();
        ReviewRoleEnum currentRole = userReviewsRoles.get(reviewId);
        if (currentRole == null) {
            return false;
        }
        return screeningAccessRoles.contains(currentRole);
    }

    public boolean hasViewAccess(Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        if (review.getIsPublic()) {
            return true;
        }

        Map<Long, ReviewRoleEnum> userReviewsRoles = ((UserPrincipal) this.getPrincipal()).getUserReviewsRoles();
        ReviewRoleEnum currentRole = userReviewsRoles.get(reviewId);
        return currentRole != null;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
