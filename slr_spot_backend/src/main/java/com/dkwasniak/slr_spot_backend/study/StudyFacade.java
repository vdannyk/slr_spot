package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.file.FileService;
import com.dkwasniak.slr_spot_backend.imports.ImportService;
import com.dkwasniak.slr_spot_backend.imports.dto.ImportRequest;
import com.dkwasniak.slr_spot_backend.study.mapper.CsvToStudyMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Component
@RequiredArgsConstructor
public class StudyFacade {

    private final StudyService studyService;
    private final FileService fileService;
    private final ImportService importService;

    public void loadStudiesFromFile(MultipartFile file, String source, String searchValue) {
        String contentType = file.getContentType();
        loadStudiesFromCsv(file, source);
    }

    public void loadStudiesFromCsv(MultipartFile file, String source) {
        List<CSVRecord> records = fileService.loadFromCsv(file);
        List<Study> studies = CsvToStudyMapper.csvToStudies(records, source);
        studyService.saveAll(studies);
    }

}
