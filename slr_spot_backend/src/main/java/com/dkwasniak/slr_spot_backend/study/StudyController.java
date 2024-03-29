package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.dto.CommentDto;
import com.dkwasniak.slr_spot_backend.comment.dto.CommentRequest;
import com.dkwasniak.slr_spot_backend.study.document.Document;
import com.dkwasniak.slr_spot_backend.operation.Operation;
import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import com.dkwasniak.slr_spot_backend.screeningDecision.dto.ScreeningDecisionDto;
import com.dkwasniak.slr_spot_backend.study.dto.DuplicatesDto;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointConstants.API_PATH + "/studies")
public class StudyController {

    private final StudyFacade studyFacade;

    @GetMapping("/{id}")
    public ResponseEntity<Study> getStudyById(@PathVariable Long id) {
        return ResponseEntity.ok().body(studyFacade.getStudyById(id));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping
    public ResponseEntity<Page<Study>> getStudies(@RequestParam("reviewId") Long reviewId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam("sortProperty") SortProperty sortProperty,
                                                @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok(studyFacade.getStudiesByReviewId(reviewId, page, size, sortProperty, sortDirection));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/to-be-reviewed")
    public ResponseEntity<Page<Study>> getStudiesToBeReviewed(@RequestParam("reviewId") Long reviewId,
                                                              @RequestParam("userId") Long userId,
                                                              @RequestParam Stage stage,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam("sortProperty") SortProperty sortProperty,
                                                              @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok(studyFacade.getStudiesToBeReviewed(reviewId, userId, stage, page, size, sortProperty, sortDirection));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/conflicted")
    public ResponseEntity<Page<Study>> getStudiesConflicted(@RequestParam("reviewId") Long reviewId,
                                                            @RequestParam Stage stage,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam("sortProperty") SortProperty sortProperty,
                                                            @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok(studyFacade.getStudiesConflicted(reviewId, stage, page, size, sortProperty, sortDirection));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/awaiting")
    public ResponseEntity<Page<Study>> getStudiesAwaiting(@RequestParam("reviewId") Long reviewId,
                                                            @RequestParam("userId") Long userId,
                                                            @RequestParam Stage stage,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam("sortProperty") SortProperty sortProperty,
                                                          @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok(studyFacade.getStudiesAwaiting(reviewId, userId, stage, page, size, sortProperty, sortDirection));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/excluded")
    public ResponseEntity<Page<Study>> getStudiesExcluded(@RequestParam("reviewId") Long reviewId,
                                                          @RequestParam Stage stage,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam("sortProperty") SortProperty sortProperty,
                                                          @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok(studyFacade.getStudiesExcluded(reviewId, stage, page, size, sortProperty, sortDirection));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Study>> removeStudyById(@PathVariable Long id) {
        studyFacade.removeStudyById(id);
        return ResponseEntity.noContent().build();
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/duplicates")
    public ResponseEntity<Page<Study>> getDuplicates(@RequestParam Long reviewId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(studyFacade.getDuplicates(reviewId, page, size));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/included")
    public ResponseEntity<Page<Study>> getIncludedStudies(@RequestParam Long reviewId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam("sortProperty") SortProperty sortProperty,
                                                          @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok().body(studyFacade.getIncludedStudies(reviewId, page, size, sortProperty, sortDirection));
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
                                                               @RequestParam Long userId,
                                                               @RequestParam Stage stage) {
        return ResponseEntity.ok().body(studyFacade.getScreeningDecisionByUser(id, userId, stage));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreStudy(@PathVariable Long id) {
        studyFacade.restoreStudy(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/duplicate")
    public ResponseEntity<Void> markStudyAsDuplicate(@PathVariable Long id) {
        studyFacade.markStudyAsDuplicate(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/duplicate")
    public ResponseEntity<Void> markStudiesAsDuplicate(@RequestBody DuplicatesDto duplicatesDto) {
        studyFacade.markStudiesAsDuplicate(duplicatesDto.getStudiesId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/full-text")
    @Transactional
    public ResponseEntity<byte[]> getFullTextDocument(@PathVariable Long id) {
        Document document = studyFacade.getFullTextDocument(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getName())
                .body(document.getData());
    }

    @GetMapping("/{id}/full-text/name")
    @Transactional
    public ResponseEntity<String> getFullTextDocumentName(@PathVariable Long id) {
        String documentName = studyFacade.getFullTextDocumentName(id);
        return ResponseEntity.ok(documentName);
    }

    @PostMapping("/{id}/full-text")
    @Transactional
    public ResponseEntity<Void> addFullTextDocument(@PathVariable Long id,
                                                    @RequestParam("file") MultipartFile file) {
        studyFacade.addFullTextDocument(id, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/full-text")
    @Transactional
    public ResponseEntity<Void> deleteFullTextDocument(@PathVariable Long id) {
        studyFacade.deleteFullTextDocument(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getFullTextDocumentName(@RequestParam Long reviewId,
                                                           @RequestParam String status) {
        int studiesCount = studyFacade.getStudiesCountByStatus(reviewId, status);
        return ResponseEntity.ok(studiesCount);
    }

    @GetMapping("/{status}/{format}")
    public ResponseEntity<Resource> exportStudiesByStatus(@RequestParam Long reviewId,
                                                          @PathVariable String status,
                                                          @PathVariable String format) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Resource studies = studyFacade.exportStudiesByStatus(reviewId, status, format);
        if ("CSV".equals(format)) {
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        }
        if ("BIB".equals(format)) {
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/x-bibtex");
        }
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(studies);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<Operation>> getStudyHistory(@PathVariable Long id) {
        return ResponseEntity.ok(studyFacade.getStudyHistory(id));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/by-folder/{folderId}")
    public ResponseEntity<Page<Study>> getStudiesByFolderId(@PathVariable Long folderId,
                                                            @RequestParam Long reviewId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(studyFacade.getStudiesByFolderId(folderId, reviewId, page, size));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/to-be-reviewed/by-folder/{folderId}")
    public ResponseEntity<Page<Study>> getStudiesToBeReviewedByFolderId(@PathVariable Long folderId,
                                                                        @RequestParam("reviewId") Long reviewId,
                                                                        @RequestParam("userId") Long userId,
                                                                        @RequestParam("stage") Stage stage,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(studyFacade.getStudiesToBeReviewedByFolderId(reviewId, folderId, userId, stage, page, size));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/conflicted/by-folder/{folderId}")
    public ResponseEntity<Page<Study>> getStudiesConflictedByFolderId(@PathVariable Long folderId,
                                                                      @RequestParam("reviewId") Long reviewId,
                                                                      @RequestParam("stage") Stage stage,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(studyFacade.getStudiesConflictedByFolderId(reviewId, folderId, stage, page, size));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/awaiting/by-folder/{folderId}")
    public ResponseEntity<Page<Study>> getStudiesAwaitingByFolderId(@PathVariable Long folderId,
                                                                    @RequestParam("reviewId") Long reviewId,
                                                                    @RequestParam("userId") Long userId,
                                                                    @RequestParam("stage") Stage stage,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(studyFacade.getStudiesAwaitingByFolderId(reviewId, folderId, userId, stage, page, size));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/excluded/by-folder/{folderId}")
    public ResponseEntity<Page<Study>> getStudiesExcludedByFolderId(@PathVariable Long folderId,
                                                                    @RequestParam("reviewId") Long reviewId,
                                                                    @RequestParam("stage") Stage stage,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(studyFacade.getStudiesExcludedByFolderId(reviewId, folderId, stage, page, size));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/to-be-reviewed/search")
    public ResponseEntity<Page<Study>> searchStudiesToBeReviewed(@RequestParam("reviewId") Long reviewId,
                                                            @RequestParam("userId") Long userId,
                                                            @RequestParam("stage") Stage stage,
                                                            @RequestParam("searchType") StudySearchType searchType,
                                                            @RequestParam("searchValue") String value,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam("sortProperty") SortProperty sortProperty,
                                                             @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok(studyFacade.searchStudiesToBeReviewed(reviewId, userId, stage, searchType, value, page, size, sortProperty, sortDirection));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/conflicted/search")
    public ResponseEntity<Page<Study>> searchStudiesConflicted(@RequestParam("reviewId") Long reviewId,
                                                            @RequestParam("stage") Stage stage,
                                                            @RequestParam("searchType") StudySearchType searchType,
                                                            @RequestParam("searchValue") String value,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam("sortProperty") SortProperty sortProperty,
                                                            @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok(studyFacade.searchStudiesConflicted(reviewId, stage, searchType, value, page, size, sortProperty, sortDirection));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/awaiting/search")
    public ResponseEntity<Page<Study>> searchStudiesAwaiting(@RequestParam("reviewId") Long reviewId,
                                                             @RequestParam("userId") Long userId,
                                                             @RequestParam("stage") Stage stage,
                                                             @RequestParam("searchType") StudySearchType searchType,
                                                             @RequestParam("searchValue") String value,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam("sortProperty") SortProperty sortProperty,
                                                             @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok(studyFacade.searchStudiesAwaiting(reviewId, userId, stage, searchType, value, page, size, sortProperty, sortDirection));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/excluded/search")
    public ResponseEntity<Page<Study>> searchStudiesExcluded(@RequestParam("reviewId") Long reviewId,
                                                               @RequestParam("stage") Stage stage,
                                                               @RequestParam("searchType") StudySearchType searchType,
                                                               @RequestParam("searchValue") String value,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam("sortProperty") SortProperty sortProperty,
                                                               @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok(studyFacade.searchStudiesExcluded(reviewId, stage, searchType, value, page, size, sortProperty, sortDirection));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/included/search")
    public ResponseEntity<Page<Study>> searchStudiesIncluded(@RequestParam("reviewId") Long reviewId,
                                                             @RequestParam("searchType") StudySearchType searchType,
                                                             @RequestParam("searchValue") String value,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam("sortProperty") SortProperty sortProperty,
                                                             @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok(studyFacade.searchStudiesIncluded(reviewId, searchType, value, page, size, sortProperty, sortDirection));
    }

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping("/search")
    public ResponseEntity<Page<Study>> searchStudies(@RequestParam("reviewId") Long reviewId,
                                                     @RequestParam("searchType") StudySearchType searchType,
                                                     @RequestParam("searchValue") String value,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam("sortProperty") SortProperty sortProperty,
                                                     @RequestParam("sortDirection") Sort.Direction sortDirection) {
        return ResponseEntity.ok(studyFacade.searchStudies(reviewId, searchType, value, page, size, sortProperty, sortDirection));
    }

}
