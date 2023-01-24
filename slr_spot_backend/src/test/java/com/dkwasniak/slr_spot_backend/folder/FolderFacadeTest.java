package com.dkwasniak.slr_spot_backend.folder;

import com.dkwasniak.slr_spot_backend.folder.dto.FolderRequest;
import com.dkwasniak.slr_spot_backend.folder.exception.FolderAlreadyExistsException;
import com.dkwasniak.slr_spot_backend.folder.exception.InvalidFolderNameException;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FolderFacadeTest {

    @InjectMocks
    private FolderFacade folderFacade;

    @Mock
    private FolderService folderService;
    @Mock
    private ReviewService reviewService;

    @Test
    public void addFolder_shouldAddFolderWithoutParent_whenParentIdNotProvided() {
        var folderRequest = new FolderRequest("test", null, 1L);

        doReturn(false).when(folderService).isExistingFolder(any(), any(), any());
        when(reviewService.getReviewById(1L)).thenReturn(new Review());
        when(folderService.saveFolder(any())).thenReturn(1L);
        var folder = folderFacade.addFolder(folderRequest);

        assertEquals(1L, folder);
    }

    @Test
    public void addFolder_shouldAddFolderWithParent_whenParentIdProvided() {
        var folderRequest = new FolderRequest("test", 1L, 1L);

        when(folderService.isExistingFolder(anyString(), anyLong(), anyLong())).thenReturn(false);
        when(folderService.getFolderById(1L)).thenReturn(new Folder());
        when(reviewService.getReviewById(1L)).thenReturn(new Review());
        when(folderService.saveFolder(any())).thenReturn(2L);
        var folder = folderFacade.addFolder(folderRequest);

        assertEquals(2L, folder);
    }

    @Test
    public void addFolder_shouldThrowFolderAlreadyExistsException_whenFolderExists() {
        var folderRequest = new FolderRequest("test", 1L, 1L);

        when(folderService.isExistingFolder(anyString(), anyLong(), anyLong())).thenReturn(true);

        assertThrows(FolderAlreadyExistsException.class,
                () -> folderFacade.addFolder(folderRequest));
    }

    @Test
    public void addFolder_shouldThrowInvalidFolderNameException_whenFolderNameEmpty() {
        var folderRequest = new FolderRequest("", 1L, 1L);

        when(folderService.isExistingFolder(anyString(), anyLong(), anyLong())).thenReturn(false);

        assertThrows(InvalidFolderNameException.class,
                () -> folderFacade.addFolder(folderRequest));
    }

    @Test
    public void removeFolderById_shouldRemoveFolder() {
        doNothing().when(folderService).removeFolderById(anyLong());
        folderFacade.removeFolderById(1L);

        verify(folderService, times(1)).removeFolderById(anyLong());
    }
}
