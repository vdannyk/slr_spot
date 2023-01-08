package com.dkwasniak.slr_spot_backend.security;

import com.dkwasniak.slr_spot_backend.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private final ReviewService reviewService;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new CustomMethodSecurityExpressionHandler(reviewService);
    }
}
