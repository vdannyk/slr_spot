package com.dkwasniak.slr_spot_backend.user;


import com.dkwasniak.slr_spot_backend.review.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserFacade userFacade;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReviewService reviewService;

    @Test
    void createUser_shouldReturnCreatedHttpStatus_whenUserCreated() throws Exception {
        var user = new User("test", "test", "test@gmail.com", "123", new ArrayList<>());

        when(userFacade.createUser(any())).thenReturn(1L);

        this.mockMvc.perform(post("/api/users/save")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

}
