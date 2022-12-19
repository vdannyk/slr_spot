package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.comment.dto.CommentDto;
import com.dkwasniak.slr_spot_backend.comment.dto.CommentRequest;
import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import com.dkwasniak.slr_spot_backend.screeningDecision.dto.ScreeningDecisionDto;
import com.dkwasniak.slr_spot_backend.study.dto.ExtractionRequest;
import com.dkwasniak.slr_spot_backend.study.dto.ExtractionResponse;
import com.dkwasniak.slr_spot_backend.study.status.StatusEnum;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointConstants.API_PATH + "/studies")
public class StudyController {

    private final StudyFacade studyFacade;

    @GetMapping
    public ResponseEntity<List<Study>> getStudiesByReviewId(@RequestParam("reviewId") Long reviewId) {
        return ResponseEntity.ok(studyFacade.getStudiesByReviewId(reviewId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Study>> removeStudyById(@PathVariable Long id) {
        studyFacade.removeStudyById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/to-review")
    public ResponseEntity<List<Study>> getStudiesToBeReviewed(@RequestParam("reviewId") Long reviewId,
                                                              @RequestParam("userId") Long userId,
                                                              @RequestParam("status") StatusEnum status) {
        return ResponseEntity.ok(studyFacade.getStudiesToBeReviewed(reviewId, userId, status));
    }

    @GetMapping("/conflicted")
    public ResponseEntity<List<Study>> getStudiesConflicted(@RequestParam("reviewId") Long reviewId,
                                                            @RequestParam("status") StatusEnum status) {
        return ResponseEntity.ok(studyFacade.getStudiesConflicted(reviewId, status));
    }

    @GetMapping("/awaiting")
    public ResponseEntity<List<Study>> getStudiesAwaiting(@RequestParam("reviewId") Long reviewId,
                                                          @RequestParam("userId") Long userId,
                                                          @RequestParam("status") StatusEnum status) {
        return ResponseEntity.ok(studyFacade.getStudiesAwaiting(reviewId, userId, status));
    }

    @GetMapping("/excluded")
    public ResponseEntity<List<Study>> getStudiesExcluded(@RequestParam("reviewId") Long reviewId,
                                                          @RequestParam("status") StatusEnum status) {
        return ResponseEntity.ok(studyFacade.getStudiesExcluded(reviewId, status));
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<Set<Tag>> getTagsByStudy(@PathVariable Long id) {
        return ResponseEntity.ok().body(studyFacade.getStudyTags(id));
    }

    @PostMapping("/{id}/tags")
    public ResponseEntity<Void> addTagToStudy(@PathVariable Long id, @RequestParam Long tagId) {
        studyFacade.addTagToStudy(id, tagId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Void> removeTagFromStudy(@PathVariable Long id,
                                                       @PathVariable Long tagId) {
        studyFacade.removeTagFromStudy(id, tagId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByStudy(@PathVariable Long id) {
        return ResponseEntity.ok().body(studyFacade.getStudyComments(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> addCommentToStudy(@PathVariable Long id,
                                                  @RequestBody CommentRequest commentRequest) {
        studyFacade.addCommentToStudy(id, commentRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/screening_decisions")
    public ResponseEntity<Void> addScreeningDecision(@PathVariable Long id,
                                                     @RequestBody ScreeningDecisionDto screeningDecisionDto) {
        studyFacade.addStudyScreeningDecision(id, screeningDecisionDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/screening_decision")
    public ResponseEntity<Decision> getScreeningDecisionByUser(@PathVariable Long id,
                                                               @RequestParam Long userId) {
        return ResponseEntity.ok().body(studyFacade.getScreeningDecisionByUser(id, userId));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Decision> restoreStudy(@PathVariable Long id, @RequestParam StatusEnum status) {
        studyFacade.restoreStudy(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/duplicates")
    public ResponseEntity<List<Study>> getDuplicates(@RequestParam Long reviewId) {
        return ResponseEntity.ok().body(studyFacade.getDuplicates(reviewId));
    }

    @GetMapping("/included")
    public ResponseEntity<List<Study>> getIncludedStudies(@RequestParam Long reviewId) {
        return ResponseEntity.ok().body(studyFacade.getIncludedStudies(reviewId));
    }

    @PutMapping("/{id}/duplicate")
    public ResponseEntity<Decision> markStudyAsDuplicate(@PathVariable Long id) {
        studyFacade.markStudyAsDuplicate(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/extract_data")
    public ResponseEntity<byte[]> extractData(@RequestBody ExtractionRequest extractionRequest) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.Builder.create().setHeader("title").build();
        CSVPrinter printer = new CSVPrinter(new PrintWriter(baos), format);
        for (var study : extractionRequest.getStudies()) {
            printer.printRecord(study.getTitle());
        }
        printer.flush();

        byte[] data = baos.toByteArray();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "data.csv");
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        return ResponseEntity.ok().headers(httpHeaders).body(data);
    }
}
