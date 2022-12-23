package com.dkwasniak.slr_spot_backend.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow();
    }


    public Set<Tag> getTagsByReviewId(Long reviewId) {
        return tagRepository.findByReview_Id(reviewId);
    }

    public Long saveTag(Tag tag) {
        return tagRepository.save(tag).getId();
    }

    public void removeTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }

}
