package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.study.exception.StudyNotFoundException;
import com.dkwasniak.slr_spot_backend.study.mapper.StudyMapper;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public Study getStudyById(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException("Study not found"));
    }

    public void saveAll(List<Study> studies) {
        studyRepository.saveAll(studies);
    }

    public List<Study> saveStudiesFromCsv(List<CSVRecord> records, String source) {
        List<Study> studies = StudyMapper.csvToStudies(records, source);
        saveAll(studies);
        return studies;
    }

    public List<Study> saveStudiesFromBib(BibTeXDatabase records) {
        List<Study> studies = StudyMapper.bibToStudies(records);
        saveAll(studies);
        return studies;
    }

    public List<Study> getStudiesToBeReviewed(Review review, User user) {
        return studyRepository.findAllByStudyImport_Review_AndScreeningDecisions_Empty(review);
    }

    public Set<Tag> getStudyTags(Study study) {
        return study.getTags();
    }

    public void addTagToStudy(Study study, Tag tag) {
        study.addTag(tag);
        studyRepository.save(study);
    }

    public void removeTagFromStudy(Study study, Tag tag) {
        study.removeTag(tag);
        studyRepository.save(study);
    }

    public List<Comment> getStudyComments(Study study) {
        return study.getComments();
    }

    public void addCommentToStudy(Study study, Comment comment) {
        study.addComment(comment);
        studyRepository.save(study);
    }
}
