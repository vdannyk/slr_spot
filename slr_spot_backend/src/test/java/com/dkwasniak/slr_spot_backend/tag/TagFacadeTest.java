package com.dkwasniak.slr_spot_backend.tag;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TagFacadeTest {

    @InjectMocks
    private TagFacade tagFacade;

    @Mock
    private TagService tagService;
    @Mock
    private ReviewService reviewService;

    @Test
    public void getTagsByReviewId_shouldReturnReviewTags_whenExists() {
        var tags = Set.of(new Tag("test"), new Tag("test2"));

        when(tagService.getTagsByReviewId(1L)).thenReturn(tags);
        var tagFound = tagFacade.getTagsByReviewId(1L);

        assertEquals(2, tagFound.size());
    }

    @Test
    public void addTag_shouldReturnNewTagId_whenAdded() {
        var tag = new Tag("test");
        tag.setId(1L);

        when(reviewService.getReviewById(1L)).thenReturn(new Review());
        when(tagService.saveTag(any())).thenReturn(tag.getId());
        var tagId = tagFacade.addTag(1L, tag.getName());

        assertEquals(1L, tagId);
    }
}
