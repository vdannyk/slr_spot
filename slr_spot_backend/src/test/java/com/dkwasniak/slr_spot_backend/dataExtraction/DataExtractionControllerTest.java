package com.dkwasniak.slr_spot_backend.dataExtraction;

import com.dkwasniak.slr_spot_backend.dataExtraction.dto.ExtractionRequest;
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
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(DataExtractionController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class DataExtractionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DataExtractionFacade dataExtractionFacade;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void extractData_shouldReturnExtractedData_whenExtractionPerformed() throws Exception {
        var extractionRq = new ExtractionRequest(new ArrayList<>(), new ArrayList<>());
        var resource = new InputStreamResource(new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray()));

        when(dataExtractionFacade.extractData(extractionRq)).thenReturn(resource);

        this.mockMvc.perform(post("/api/v1/data_extraction")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(extractionRq)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
