package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.user.UserFacade;
import com.dkwasniak.slr_spot_backend.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ReviewController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReviewControllerTest {

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
    @MockBean
    private ReviewFacade reviewFacade;

    @Test
    void createUser_shouldReturnCreatedHttpStatus_whenUserCreated() throws Exception {
//        var reviewDto = new ReviewDto("test", "test", "test", true, 2);
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn("test@gmail.com");
//        SecurityContext securityContext = mock(SecurityContext.class);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        when(reviewFacade.createReview(any(), anyString())).thenReturn(1L);
//
//        this.mockMvc.perform(post("/api/reviews/save")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(reviewDto)))
//                .andDo(print())
//                .andExpect(status().isOk());
    }

//    @Test
//    void getAllProjects_shouldReturnProjects_whenExists() throws Exception {
//        when(reviewService.getReviews()).thenReturn(new ArrayList<>() {{
//            add(new Review("test", "test", "test", true, 2));
//            add(new Review("test", "test", "test", true, 2));
//        }});
//        this.mockMvc.perform(get("/api/reviews")).andDo(print())
//                .andExpect(status().isOk());
//    }

}
