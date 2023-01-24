package com.dkwasniak.slr_spot_backend.tag;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
@WebMvcTest(TagController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TagFacade tagFacade;

    @Test
    void getTags_shouldReturnHttpOkStatus_whenTagsFound() throws Exception {
        var tags = Set.of(new Tag("test"), new Tag("test2"));

        when(tagFacade.getTagsByReviewId(1L)).thenReturn(tags);

        this.mockMvc.perform(get("/api/v1/tags")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void addTag_shouldReturnCreatedHttpStatus_whenTagCreated() throws Exception {
        var tag = new Tag("test");
        tag.setId(1L);

        when(tagFacade.addTag(1L, tag.getName())).thenReturn(tag.getId());

        this.mockMvc.perform(post("/api/v1/tags")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1")
                        .param("name", "test"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", is(1)));
    }

    @Test
    void removeTag_shouldReturnHttpOkStatus_whenRemoved() throws Exception {
        doNothing().when(tagFacade).removeTag(1L);

        this.mockMvc.perform(delete("/api/v1/tags/1")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
