package com.dkwasniak.slr_spot_backend.dataExtraction;

import com.dkwasniak.slr_spot_backend.study.Study;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DataExtractionUtilsTest {

    @Test
    public void getField_shouldReturnStudyField_whenAllowed() {
        var study = new Study();
        study.setAuthors("testAuthors");
        study.setTitle("testTitle");
        study.setJournalTitle("testJournal");
        study.setDocumentAbstract("testAbstract");
        study.setPublicationYear(2000);
        study.setDoi("testDoi");

        assertEquals("testAuthors", DataExtractionUtils.getField(study, ExtractionField.AUTHORS));
        assertEquals("testTitle", DataExtractionUtils.getField(study, ExtractionField.TITLE));
        assertEquals("testJournal", DataExtractionUtils.getField(study, ExtractionField.JOURNAL));
        assertEquals("testAbstract", DataExtractionUtils.getField(study, ExtractionField.ABSTRACT));
        assertEquals("2000", DataExtractionUtils.getField(study, ExtractionField.PUBLICATIONYEAR));
        assertEquals("testDoi", DataExtractionUtils.getField(study, ExtractionField.DOI));
    }

}
