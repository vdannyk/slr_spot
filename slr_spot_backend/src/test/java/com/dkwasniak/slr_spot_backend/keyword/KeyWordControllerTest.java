package com.dkwasniak.slr_spot_backend.keyword;

import com.dkwasniak.slr_spot_backend.criterion.CriterionType;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWord;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWordController;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWordFacade;
import com.dkwasniak.slr_spot_backend.keyWord.dto.KeyWordDto;
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

import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(KeyWordController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class KeyWordControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private KeyWordFacade keyWordFacade;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getKeywords_shouldReturnKeywords_whenExists() throws Exception {
        var keywords = Set.of(new KeyWord(), new KeyWord());

        when(keyWordFacade.getKeyWords(any())).thenReturn(keywords);

        this.mockMvc.perform(get("/api/v1/keywords")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getUserKeywords_shouldReturnKeywords_whenExists() throws Exception {
        var keywords = Set.of(new KeyWord(), new KeyWord());

        when(keyWordFacade.getKeyWords(any(), anyLong())).thenReturn(keywords);

        this.mockMvc.perform(get("/api/v1/keywords/personal")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void addKeyword_shouldReturnCreatedHttpStatus_whenKeywordCreated() throws Exception {
        var keyword = new KeyWord("test", CriterionType.INCLUSION);
        keyword.setId(1L);
        var keyWordDto = new KeyWordDto(1L, 1L, keyword.getName(), keyword.getType().name());

        when(keyWordFacade.addKeyWord(keyWordDto)).thenReturn(keyword.getId());

        this.mockMvc.perform(post("/api/v1/keywords")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(keyWordDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void addUserKeyword_shouldReturnCreatedHttpStatus_whenKeywordCreated() throws Exception {
        var keyword = new KeyWord("test", CriterionType.INCLUSION);
        keyword.setId(1L);
        var keyWordDto = new KeyWordDto(1L, 1L, keyword.getName(), keyword.getType().name());

        when(keyWordFacade.addKeyWord(keyWordDto)).thenReturn(keyword.getId());

        this.mockMvc.perform(post("/api/v1/keywords/personal")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(keyWordDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void removeKeyword_shouldReturnHttpOkStatus_whenRemoved() throws Exception {
        doNothing().when(keyWordFacade).removeKeyWord(1L);

        this.mockMvc.perform(delete("/api/v1/keywords/1")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
