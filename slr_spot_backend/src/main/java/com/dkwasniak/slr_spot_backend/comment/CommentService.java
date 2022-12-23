package com.dkwasniak.slr_spot_backend.comment;

import com.dkwasniak.slr_spot_backend.comment.dto.CommentDto;
import com.dkwasniak.slr_spot_backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    public CommentDto toCommentDto(Comment comment) {
        User user = comment.getUser();
        return CommentDto.builder()
                .date(comment.getDate())
                .content(comment.getContent())
                .author(String.format("%s %s", user.getFirstName(), user.getLastName()))
                .build();

    }
}
