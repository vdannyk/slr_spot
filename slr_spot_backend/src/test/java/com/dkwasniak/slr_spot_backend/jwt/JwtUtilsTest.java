package com.dkwasniak.slr_spot_backend.jwt;

import com.dkwasniak.slr_spot_backend.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "jwt.secret=secret")
public class JwtUtilsTest {

    @Test
    public void generationOfJwtTokenTest() {
        var user = new User("test", "test", "test@gmail.com", "123");
        var httpServletRq = new MockHttpServletRequest();
        httpServletRq.setServletPath("testPath");
        new JwtUtils().setSecretStatic("test");
        var jwt = JwtUtils.generateJwt(user, httpServletRq);
        var decoded = JwtUtils.validateJwt(jwt);

        assertEquals("test@gmail.com", JwtUtils.getUsername(decoded));
    }

    @Test
    public void generationOfRefreshJwtTokenTest() {
        var user = new User("test", "test", "test@gmail.com", "123");
        var httpServletRq = new MockHttpServletRequest();
        httpServletRq.setServletPath("testPath");
        new JwtUtils().setSecretStatic("test");
        var jwt = JwtUtils.generateRefreshToken(user, httpServletRq);
        var decoded = JwtUtils.validateJwt(jwt);

        assertEquals("test@gmail.com", JwtUtils.getUsername(decoded));
    }

    @Test
    public void validateHeader() {
        assertTrue(JwtUtils.validateHeader("Bearer jwt"));
    }

}
