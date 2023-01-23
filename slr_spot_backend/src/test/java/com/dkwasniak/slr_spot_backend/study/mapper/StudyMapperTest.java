package com.dkwasniak.slr_spot_backend.study.mapper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.jbibtex.BibTeXParser;
import org.jbibtex.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StudyMapperTest {

    private static final String CSV_EXAMPLE = "title,authors,publication year\n" +
            "test,test2,2012";

    private static final String BIB_EXAMPLE = "@ARTICLE{Feng20232892,\n" +
            "author={test},\n" +
            "title={title},\n" +
            "year={2023},\n" +
            "}";

    @Test
    public void csvToStudies() throws IOException {
        var stream = new ByteArrayInputStream(CSV_EXAMPLE.getBytes(StandardCharsets.UTF_8));
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(bufferedReader,
                CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).setTrim(true).setIgnoreHeaderCase(true).build());

        var res = StudyMapper.csvToStudies(csvParser.getRecords(), "DEFAULT");

        assertEquals(1, res.size());
    }

    @Test
    public void bibToStudies() throws IOException, ParseException {
        var stream = new ByteArrayInputStream(BIB_EXAMPLE.getBytes(StandardCharsets.UTF_8));
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8));
        BibTeXParser bibTeXParser = new BibTeXParser();

        var res = StudyMapper.bibToStudies(bibTeXParser.parse(bufferedReader));

        assertEquals(1, res.size());
    }
}
