package com.dkwasniak.slr_spot_backend.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.dkwasniak.slr_spot_backend.imports.ImportContext;
import com.dkwasniak.slr_spot_backend.imports.ImportController;
import com.dkwasniak.slr_spot_backend.imports.ImportFacade;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(JwtController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class JwtControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    void refreshToken_shouldReturnJwtResponse_whenCorrect() throws Exception {
        var user = new User("test", "test", "test@gmail.com", "123");
        user.setId(1L);
        var refreshRq = new RefreshTokenRequest();
        refreshRq.setRefreshToken("test");
        var mockUtil = mockStatic(JwtUtils.class);

        mockUtil.when(() -> JwtUtils.validateHeader(anyString())).thenReturn(true);
        mockUtil.when(() -> JwtUtils.validateJwt(anyString())).thenReturn(mock(DecodedJWT.class));
        mockUtil.when(() -> JwtUtils.getUsername(any())).thenReturn("test@gmail.com");
        mockUtil.when(() -> JwtUtils.generateJwt(any(), any())).thenReturn("jwt");
        when(userService.getUserByEmail(anyString())).thenReturn(user);

        this.mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRq))
                        .header("AUTHORIZATION", "Bearer test"))
                .andDo(print())
                .andExpect(jsonPath("$.accessToken", is("jwt")))
                .andExpect(jsonPath("$.username", is("test@gmail.com")));
    }
}
