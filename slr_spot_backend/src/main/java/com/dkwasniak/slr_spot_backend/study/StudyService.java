package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.study.mapper.StudyMapper;
import com.dkwasniak.slr_spot_backend.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

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
}
