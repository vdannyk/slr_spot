package com.dkwasniak.slr_spot_backend.comment;

import com.dkwasniak.slr_spot_backend.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Test
    public void toCommentDto_shouldReturnCommentDto_whenCreated() {
        var user = new User("test", "test", "test@gmail.com", "123");
        var comment = new Comment("test");
        comment.setUser(user);

        var commentDto = commentService.toCommentDto(comment);

        assertEquals("test test", commentDto.getAuthor());
    }
}
