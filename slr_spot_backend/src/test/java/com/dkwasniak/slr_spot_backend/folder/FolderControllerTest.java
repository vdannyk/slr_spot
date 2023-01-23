package com.dkwasniak.slr_spot_backend.folder;

import com.dkwasniak.slr_spot_backend.folder.dto.FolderAssignDto;
import com.dkwasniak.slr_spot_backend.folder.dto.FolderRequest;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
@WebMvcTest(FolderController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class FolderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FolderFacade folderFacade;
    @MockBean
    private FolderService folderService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getFolders_shouldReturnReviewFolders_whenExists() throws Exception {
        List<Folder> folders = List.of(new Folder("test1"), new Folder("test2"));

        when(folderService.getAllFoldersByReviewId(any())).thenReturn(folders);

        this.mockMvc.perform(get("/api/v1/folders")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getFolderTree_shouldReturnFolderTree_whenExistsRootFolders() throws Exception {
        List<Folder> folders = List.of(new Folder("test1"), new Folder("test2"));

        when(folderService.getRootFolders(any())).thenReturn(folders);

        this.mockMvc.perform(get("/api/v1/folders/tree")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void addFolder_shouldReturnHttpCreatedStatus_whenAdded() throws Exception {
        var rq = new FolderRequest();

        when(folderFacade.addFolder(any())).thenReturn(1L);

        this.mockMvc.perform(post("/api/v1/folders")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rq)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void addStudies_shouldReturnHttpOkStatus_whenAdded() throws Exception {
        var rq = new FolderAssignDto();

        doNothing().when(folderService).addStudies(anyLong(), any());

        this.mockMvc.perform(post("/api/v1/folders/1/studies")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rq)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteFolder_shouldReturnHttpOkStatus_whenRemoved() throws Exception {
        doNothing().when(folderFacade).removeFolderById(anyLong());

        this.mockMvc.perform(delete("/api/v1/folders/1")
                        .contentType(APPLICATION_JSON)
                        .param("reviewId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
