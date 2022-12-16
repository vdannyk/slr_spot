package com.dkwasniak.slr_spot_backend.keyWord;

import com.dkwasniak.slr_spot_backend.keyWord.dto.KeyWordDto;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(EndpointConstants.API_PATH + "/keywords")
@RequiredArgsConstructor
public class KeyWordController {

    private final KeyWordFacade keyWordFacade;

    @GetMapping
    public ResponseEntity<Set<KeyWord>> getKeywords(@RequestParam Long reviewId) {
        return ResponseEntity.ok().body(keyWordFacade.getKeyWords(reviewId));
    }

    @GetMapping("/personal")
    public ResponseEntity<Set<KeyWord>> getUserKeywords(@RequestParam Long reviewId,
                                                    @RequestParam Long userId) {
        return ResponseEntity.ok().body(keyWordFacade.getKeyWords(reviewId, userId));
    }

    @PostMapping
    public ResponseEntity<Void> addKeyword(@RequestBody KeyWordDto keyWordDto) {
        keyWordFacade.addKeyWord(keyWordDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/personal")
    public ResponseEntity<Void> addUserKeyword(@RequestBody KeyWordDto keyWordDto) {
        keyWordFacade.addKeyWord(keyWordDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{keywordId}")
    public ResponseEntity<Void> removeKeyword(@PathVariable Long keywordId) {
        keyWordFacade.removeKeyWord(keywordId);
        return ResponseEntity.ok().build();
    }

}
