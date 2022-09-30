package com.dkwasniak.slr_spot_backend.security;

import com.dkwasniak.slr_spot_backend.filter.CustomAuthenticationFilter;
import com.dkwasniak.slr_spot_backend.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

        // Disable CSRF
        http.cors();
        http.csrf().disable();

//        http.formLogin()
//                .loginProcessingUrl("/api/auth/signin");

        // Set STATELESS session management
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        // Handle unathorized requests exceptions
//        http.exceptionHandling().authenticationEntryPoint(
//                ((request, response, authException) -> {
//                    response.sendError(
//                            HttpServletResponse.SC_UNAUTHORIZED,
//                            authException.getMessage()
//                    );
//                })
//        );

        // Set endpoints to authorize
        http.authorizeRequests().antMatchers(
                "/api/auth/signin/**",
                "/api/token/refresh/**",
                "/api/user/save/**"
                ).permitAll();
        http.authorizeRequests().antMatchers(GET, "/api/user/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();

        // Add authentication filter
        var customAuthFilter = new CustomAuthenticationFilter(authenticationManager);
        customAuthFilter.setFilterProcessesUrl("/api/auth/signin");
        http.addFilter(customAuthFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) {
        return http.getSharedObject(AuthenticationManager.class);
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
