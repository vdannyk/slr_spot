package com.dkwasniak.slr_spot_backend.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag getTagById(long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Tag with given id not found"));
    }
}
