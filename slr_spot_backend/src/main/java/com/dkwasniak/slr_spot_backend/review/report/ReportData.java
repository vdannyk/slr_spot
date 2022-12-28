package com.dkwasniak.slr_spot_backend.review.report;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ReportData {

    private String title;
    private String owner;
    private String researchArea;
    private String description;
    private List<String> researchQuestions;
    private List<String> members;
    private Integer totalStudiesImported;
    private Integer removedDuplicates;
    private Integer selectedStudies;

}
