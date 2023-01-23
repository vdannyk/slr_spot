package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ReviewController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReviewFacade reviewFacade;

    @Test
    void saveReview_shouldReturnCreatedHttpStatus_whenReviewCreated() throws Exception {
        var reviewDto = ReviewDto.builder().name("test").build();

        when(reviewFacade.addReview(any())).thenReturn(1L);

        this.mockMvc.perform(post("/api/v1/reviews")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void updateReview_shouldReturnOkHttpStatus_whenReviewUpdated() throws Exception {
        var reviewDto = ReviewDto.builder().name("test").build();

        doNothing().when(reviewFacade).updateReview(anyLong(), any());

        this.mockMvc.perform(put("/api/v1/reviews/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUsersAvailableToAdd_shouldReturnEmail_whenAvailable() throws Exception {
        when(reviewFacade.getUsersAvailableToAdd(anyLong())).thenReturn(Set.of("test@gmail.com"));

        this.mockMvc.perform(get("/api/v1/reviews/1/members/search")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasItem("test@gmail.com")));
    }

    @Test
    void generateReviewReport_shouldReturnOkHttpStatus_whenReportGenerated() throws Exception {
        when(reviewFacade.generateReviewReport(anyLong())).thenReturn(
                new InputStreamResource(new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray()))
        );

        this.mockMvc.perform(get("/api/v1/reviews/1/report")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
