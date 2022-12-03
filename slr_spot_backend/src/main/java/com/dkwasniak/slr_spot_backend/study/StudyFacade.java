package com.dkwasniak.slr_spot_backend.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudyFacade {

    private final StudyService studyService;

    public List<Study> parseStudies(MultipartFile file) throws IOException {
        List<Study> studies = CsvToStudyMapper.csvToStudies(file.getInputStream());
        studyService.saveAll(studies);
        return studies;
    }
}
