package com.dkwasniak.slr_spot_backend.security;

import com.dkwasniak.slr_spot_backend.review.ReviewService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionHandler
    extends DefaultMethodSecurityExpressionHandler {

    private AuthenticationTrustResolver trustResolver =
            new AuthenticationTrustResolverImpl();

    private final ReviewService reviewService;

    public CustomMethodSecurityExpressionHandler(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
            Authentication authentication, MethodInvocation invocation) {
        CustomSecurityExpressionRoot root =
                new CustomSecurityExpressionRoot(authentication, reviewService);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }
}
