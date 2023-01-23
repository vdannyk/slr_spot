package com.dkwasniak.slr_spot_backend.forgotPassword;

import com.dkwasniak.slr_spot_backend.forgotPassword.dto.EmailDto;
import com.dkwasniak.slr_spot_backend.forgotPassword.dto.ForgotPasswordDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ForgotPasswordController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class ForgotPasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ForgotPasswordFacade forgotPasswordFacade;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void resetPassword_shouldReturnOkHttpStatus_whenPasswordReset() throws Exception {
        var emailDto = new EmailDto();
        emailDto.setEmail("test@gmail.com");

        doNothing().when(forgotPasswordFacade).resetPassword(anyString());

        this.mockMvc.perform(post("/api/v1/password/forgot")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyResetPassword_shouldReturnOkHttpStatus_whenTokenValidation() throws Exception {
        doNothing().when(forgotPasswordFacade).validateToken(anyString());

        this.mockMvc.perform(get("/api/v1/password/forgot")
                        .contentType(APPLICATION_JSON)
                        .param("resetToken", "testToken"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword_shouldResetPassword_whenSecondRequest() throws Exception {
        var forgotDto = new ForgotPasswordDto();
        forgotDto.setNewPassword("testPassword");
        forgotDto.setToken("testToken");

        doNothing().when(forgotPasswordFacade).resetPassword((ForgotPasswordDto) any());

        this.mockMvc.perform(post("/api/v1/password/reset")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(forgotDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
