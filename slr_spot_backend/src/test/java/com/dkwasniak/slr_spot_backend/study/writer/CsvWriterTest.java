package com.dkwasniak.slr_spot_backend.study.writer;

import com.dkwasniak.slr_spot_backend.study.Study;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CsvWriterTest {

    @InjectMocks
    private CsvWriter csvWriter;

    @Test
    public void write_shouldWriteStudiesToResource_whenCorrect() {
        var study1 = Study.builder()
                .id(1L)
                .title("test1")
                .authors("testAuthors")
                .journalTitle("journalTitle")
                .publicationYear(2012)
                .volume("volume")
                .url("url")
                .documentAbstract("abstract")
                .issn("issn")
                .language("language")
                .doi("123")
                .build();

        var res = csvWriter.write(List.of(study1));

        assertNotNull(res);
    }
}
