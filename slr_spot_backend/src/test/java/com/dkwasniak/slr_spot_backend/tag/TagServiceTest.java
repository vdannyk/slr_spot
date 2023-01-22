package com.dkwasniak.slr_spot_backend.tag;

import com.dkwasniak.slr_spot_backend.tag.exception.TagNotFoundException;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.userReview.exception.UserReviewNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Test
    public void getTagById_shouldReturnTag_whenExists() {
        var tag = new Tag("test");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        var tagFound = tagService.getTagById(1L);

        assertEquals("test", tagFound.getName());
    }

    @Test
    public void getTagById_shouldThrowTagNotFoundException_whenNotExists() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class,
                () -> tagService.getTagById(1L));
    }

    @Test
    public void getTagsByReviewId_shouldReturnReviewTags_whenExists() {
        var tags = Set.of(new Tag("test"), new Tag("test2"));

        when(tagRepository.findByReview_Id(1L)).thenReturn(tags);
        var tagFound = tagService.getTagsByReviewId(1L);

        assertEquals(2, tagFound.size());
    }
}
