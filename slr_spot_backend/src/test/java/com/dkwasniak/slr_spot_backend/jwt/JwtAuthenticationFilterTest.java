package com.dkwasniak.slr_spot_backend.jwt;

import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserRepository;
import com.dkwasniak.slr_spot_backend.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    private static final String AUTH = "{\n" +
            "    \"username\": \"test@gmail.com\",\n" +
            "    \"password\": \"1234\"\n" +
            "}";

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void init() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(
                authenticationManager,
                userService
        );
    }

    @Test
    public void attemptAuthentication_shouldReturnAuth_whenSuccess() {
        var user = new User("test", "test", "test@gmail.com", "123");
        user.setId(1L);
        var httpServletRq = new MockHttpServletRequest();
        httpServletRq.setContent(AUTH.getBytes());
        var httpServletRs = new MockHttpServletResponse();

        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        var auth = jwtAuthenticationFilter.attemptAuthentication(httpServletRq, httpServletRs);

        assertEquals(user.getEmail(), auth.getPrincipal());
    }
}
