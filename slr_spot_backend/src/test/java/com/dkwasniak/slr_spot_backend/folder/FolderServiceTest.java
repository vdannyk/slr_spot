package com.dkwasniak.slr_spot_backend.folder;

import com.dkwasniak.slr_spot_backend.folder.exception.FolderNotFoundException;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.study.StudyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FolderServiceTest {


    @InjectMocks
    private FolderService folderService;

    @Mock
    private FolderRepository folderRepository;
    @Mock
    private StudyService studyService;

    @Test
    public void getAllFoldersByReviewId_shouldReturnAllReviewFolders_whenExists() {
        List<Folder> folders = List.of(new Folder("test1"), new Folder("test2"));

        when(folderRepository.findAllByReview_Id(1L)).thenReturn(folders);
        var folderList = folderService.getAllFoldersByReviewId(1L);

        assertEquals(2, folderList.size());
    }

    @Test
    public void getFolderById_shouldReturnFolder_whenExists() {
        var folder = new Folder("test1");

        when(folderRepository.findById(1L)).thenReturn(java.util.Optional.of(folder));
        var folderById = folderService.getFolderById(1L);

        assertEquals("test1", folderById.getName());
    }

    @Test
    public void getFolderById_shouldThrowFolderNotFoundException_whenNotExists() {
        when(folderRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(FolderNotFoundException.class,
                () -> folderService.getFolderById(1L));
    }

    @Test
    public void getRootFolders_shouldReturnAllRootFolders_whenExists() {
        List<Folder> folders = List.of(new Folder("test1"), new Folder("test2"));

        when(folderRepository.findByReview_IdAndParentIsNull(1L)).thenReturn(folders);
        var folderList = folderService.getRootFolders(1L);

        assertEquals(2, folderList.size());
    }

    @Test
    public void addStudies_shouldAddAllStudies() {
        var study1 = Study.builder().title("test1").build();
        var study2 = Study.builder().title("test2").build();
        var folder1 = new Folder("testfolder1");
        folder1.setStudies(new ArrayList<>());
        List<Long> studies = List.of(1L, 2L);

        when(folderRepository.findById(anyLong())).thenReturn(java.util.Optional.of(folder1));
        when(studyService.getStudyById(anyLong())).thenReturn(study1).thenReturn(study2);
        when(folderRepository.save(any())).thenReturn(folder1).thenReturn(folder1);
        folderService.addStudies(1L, studies);

        verify(folderRepository, times(1)).save(any());
    }

    @Test
    public void saveFolder_shouldReturnSavedFolderId_whenSaved() {
        var folder1 = new Folder("testfolder1");
        folder1.setId(1L);

        when(folderRepository.save(folder1)).thenReturn(folder1);
        var saveFolder = folderService.saveFolder(folder1);

        assertEquals(1L, saveFolder);
    }

    @Test
    public void removeFolderById_shouldRemoveFolder() {
        doNothing().when(folderRepository).deleteById(anyLong());
        folderService.removeFolderById(1L);

        verify(folderRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void isExistingFolder_shouldReturnTrue_whenExistsWithoutParent() {
        var folder1 = new Folder("testfolder1");
        folder1.setId(1L);

        when(folderRepository.existsByNameAndReview_Id(folder1.getName(), 1L)).thenReturn(true);

        assertTrue(folderService.isExistingFolder(folder1.getName(), 1L, null));
    }

    @Test
    public void isExistingFolder_shouldReturnTrue_whenExistsWithParent() {
        var folder1 = new Folder("testfolder1");
        folder1.setId(1L);

        when(folderRepository.existsByNameAndReview_IdAndParent_Id(folder1.getName(), 1L, 1L)).thenReturn(true);

        assertTrue(folderService.isExistingFolder(folder1.getName(), 1L, 1L));
    }
}
