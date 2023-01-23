package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.study.document.Document;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserController;
import com.dkwasniak.slr_spot_backend.user.UserFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(StudyController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class StudyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StudyFacade studyFacade;

    @Test
    void getFullTextDocument() throws Exception {
        var doc = new Document();
        doc.setData(new byte[0]);
        when(studyFacade.getFullTextDocument(anyLong()))
                .thenReturn(doc);

        this.mockMvc.perform(get("/api/v1/studies/1/full-text")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void exportStudiesByStatus() throws Exception {

        when(studyFacade.exportStudiesByStatus(anyLong(), anyString(), anyString()))
                .thenReturn(new InputStreamResource(new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray())));

        this.mockMvc.perform(get("/api/v1/studies/TITLE_ABSTRACT/CSV")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk());

        when(studyFacade.exportStudiesByStatus(anyLong(), anyString(), anyString()))
                .thenReturn(new InputStreamResource(new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray())));

        this.mockMvc.perform(get("/api/v1/studies/TITLE_ABSTRACT/BIB")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
