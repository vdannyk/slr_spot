package com.dkwasniak.slr_spot_backend.imports;

import com.dkwasniak.slr_spot_backend.review.ReviewController;
import com.dkwasniak.slr_spot_backend.review.ReviewFacade;
import com.dkwasniak.slr_spot_backend.review.dto.ReviewDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ImportController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class ImportControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ImportFacade importFacade;

    @Test
    void saveImport_shouldReturnCreatedHttpStatus_whenImportPerformed() throws Exception {
        var importContext = new ImportContext();

        doNothing().when(importFacade).importStudies(any());

        this.mockMvc.perform(post("/api/v1/imports")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(importContext)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getImportsByReviewId_shouldReturnImports_whenExists() throws Exception {
        var import1 = new Import();
        import1.setSource("SCOPUS");
        List<Import> imports = List.of(
                import1
        );
        when(importFacade.getImportsByReviewId(anyLong(), anyInt(), anyInt())).thenReturn(new PageImpl<>(imports));

        this.mockMvc.perform(get("/api/v1/imports")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].source", is("SCOPUS")));
    }
}
