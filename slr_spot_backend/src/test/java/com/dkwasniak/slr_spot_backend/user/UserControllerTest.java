package com.dkwasniak.slr_spot_backend.user;


import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.dkwasniak.slr_spot_backend.user.dto.UpdatePasswordDto;
import com.dkwasniak.slr_spot_backend.user.dto.UserDto;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserFacade userFacade;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SecurityContext securityContext;

    @Test
    void createUser_shouldReturnCreatedHttpStatus_whenUserCreated() throws Exception {
        var user = new User("test", "test", "test@gmail.com", "123");

        when(userFacade.createUser(any())).thenReturn(1L);

        this.mockMvc.perform(post("/api/v1/users/save")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void getUsersByReviewId_shouldReturnUserReviews_whenExists() throws Exception {
        var user = new User("test", "test", "test@gmail.com", "123");
        user.setId(1L);
        var review = new Review("test", "test", "test", true, 1);
        review.setId(1L);
        var userReviews = List.of(new UserReview(user, review, null));
        var page = new PageImpl<>(userReviews);

        when(userFacade.getUsersByReviewId(anyLong(), anyInt(), anyInt())).thenReturn(page);

        this.mockMvc.perform(get("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    void getUsersEmails_shouldReturnUsersEmails() throws Exception {
        var user = new User("test", "test", "test@gmail.com", "123");

        SecurityContextHolder.setContext(securityContext);
        var authMock = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(new UserPrincipal(user));
        when(userFacade.getEmails(anyString())).thenReturn(Set.of("test"));

        this.mockMvc.perform(get("/api/v1/users/emails")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void confirmAccount_shouldReturnOkStatus_whenConfirmed() throws Exception {
        var token = "test";

        doNothing().when(userFacade).confirmAccount(token);

        this.mockMvc.perform(get("/api/v1/users/confirm")
                        .contentType(APPLICATION_JSON)
                        .param("confirmationToken", token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updatePassword_shouldReturnOkStatus_whenCorrectRequest() throws Exception {
        var updatePasswordDto = new UpdatePasswordDto();
        var user = new User("test", "test", "test@gmail.com", "123");

        SecurityContextHolder.setContext(securityContext);
        var authMock = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(new UserPrincipal(user));
        doNothing().when(userFacade).updatePassword("test", updatePasswordDto);

        this.mockMvc.perform(post("/api/v1/users/password/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateEmail_shouldReturnOkStatus_whenCorrectRequest() throws Exception {
        var user = new User("test", "test", "test@gmail.com", "123");
        var userDto = new UserDto(1L, "test@gmail.com", "test", "test", "test");

        SecurityContextHolder.setContext(securityContext);
        var authMock = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(new UserPrincipal(user));
        doNothing().when(userFacade).updateEmail("test", userDto.getEmail());

        this.mockMvc.perform(post("/api/v1/users/email/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void saveEmail_shouldReturnOkStatus_whenCorrectRequest() throws Exception {
        var token = "test";

        doNothing().when(userFacade).saveEmail(token);

        this.mockMvc.perform(get("/api/v1/users/1/email/update/confirm")
                        .contentType(APPLICATION_JSON)
                        .param("confirmationToken", token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateName_shouldReturnOkStatus_whenCorrectRequest() throws Exception {
        var user = new User("test", "test", "test@gmail.com", "123");
        var userDto = new UserDto(1L, "test@gmail.com", "test", "test", "test");

        SecurityContextHolder.setContext(securityContext);
        var authMock = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(new UserPrincipal(user));
        doNothing().when(userFacade).updateName("test", userDto);

        this.mockMvc.perform(post("/api/v1/users/name/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void changeUserRole_shouldReturnOkStatus_whenCorrectRequest() throws Exception {
        doNothing().when(userFacade).changeUserRole(1L, 1L, "test");

        this.mockMvc.perform(put("/api/v1/users/1/role")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1")
                        .param("role", "test"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
