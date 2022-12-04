package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.file.FileService;
import com.dkwasniak.slr_spot_backend.imports.ImportService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXDatabase;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class StudyFacade {

    private final StudyService studyService;
    private final FileService fileService;
    private final ImportService importService;

    public void loadStudiesFromFile(MultipartFile file, String source, String searchValue) {
        String contentType = file.getContentType();
        List<Study> studies;
        if ("text/csv".equals(contentType)) {
            studies = loadStudiesFromCsv(file, source);
        } else if ("bib".equals(contentType)) {
            studies = loadStudiesFromBib(file);
        } else {
            studies = new ArrayList<>();
        }
    }

    public List<Study> loadStudiesFromCsv(MultipartFile file, String source) {
        List<CSVRecord> records = fileService.loadFromCsv(file);
        return studyService.saveStudiesFromCsv(records, source);
    }

    public List<Study> loadStudiesFromBib(MultipartFile file) {
        BibTeXDatabase records = fileService.loadFromBibtex(file);
//        List<Study> studies = CsvToStudyMapper.csvToStudies(records, source);
//        studyService.saveAll(studies);
        return new ArrayList<>();
    }

}
