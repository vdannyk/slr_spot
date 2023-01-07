package com.dkwasniak.slr_spot_backend.tag;

import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;


@Controller
@RequestMapping(EndpointConstants.API_PATH + "/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagFacade tagFacade;

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping
    public ResponseEntity<Set<Tag>> getTags(@RequestParam Long reviewId) {
        return ResponseEntity.ok().body(tagFacade.getTagsByReviewId(reviewId));
    }

    @PostAuthorize("hasScreeningAccess(#reviewId)")
    @PostMapping
    public ResponseEntity<Long> addTag(@RequestParam Long reviewId, @RequestParam String name) {
        long id = tagFacade.addTag(reviewId, name);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path(EndpointConstants.API_PATH + "/tags").buildAndExpand(id).toUriString());
        return ResponseEntity.created(uri).body(id);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> removeTag(@PathVariable Long tagId) {
        tagFacade.removeTag(tagId);
        return ResponseEntity.ok().build();
    }
}
