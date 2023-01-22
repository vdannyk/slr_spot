package com.dkwasniak.slr_spot_backend.criterion;

import com.dkwasniak.slr_spot_backend.criterion.dto.CriterionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(CriterionController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class CriterionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CriterionFacade criterionFacade;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SecurityContext securityContext;

    @Test
    void getCriteria_shouldReturnCriteria_whenExists() throws Exception {
        var criteria = List.of(new Criterion(), new Criterion());

        when(criterionFacade.getCriteriaByReviewId(any())).thenReturn(criteria);

        this.mockMvc.perform(get("/api/v1/criteria")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void addCriterion_shouldReturnCreatedHttpStatus_whenTagCreated() throws Exception {
        var criterion = new Criterion("test", CriterionType.INCLUSION);
        criterion.setId(1L);
        var criterionDto = new CriterionDto(1L, criterion.getName(), criterion.getType().name());

        when(criterionFacade.addCriterion(criterionDto)).thenReturn(criterion.getId());

        this.mockMvc.perform(post("/api/v1/criteria")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criterionDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void removeCriterion_shouldReturnHttpOkStatus_whenRemoved() throws Exception {
        doNothing().when(criterionFacade).removeCriterion(1L);

        this.mockMvc.perform(delete("/api/v1/criteria/1")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
