package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    public Map<Long, ReviewRoleEnum> getUserReviewsRoles() {
        final Map<Long, ReviewRoleEnum> userReviews = new HashMap<>();
        for (var userReview : user.getReviews()) {
            userReviews.put(userReview.getReview().getId(), ReviewRoleEnum.valueOf(userReview.getRole().getName()));
        }
        return userReviews;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
