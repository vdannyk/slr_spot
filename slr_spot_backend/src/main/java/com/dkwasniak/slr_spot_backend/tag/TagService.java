package com.dkwasniak.slr_spot_backend.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag getById(long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Tag with given id not found"));
    }

    public Tag getByName(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new IllegalStateException("Tag with given name not found"));
    }

    public boolean checkIfExistsByName(String name) {
        return tagRepository.existsByName(name);
    }
}
