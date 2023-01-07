package com.dkwasniak.slr_spot_backend.study.reader;

import com.dkwasniak.slr_spot_backend.file.exception.FileLoadingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RisReader {

    private static final String END_FILE = "ER";

    public List<Map<String, String>> read(MultipartFile multipartFile) {
        List<Map<String, String>> references = new ArrayList<>();
        Map<String, String> reference = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8))) {
            CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT);

            for (var risRecord : csvParser) {
                var recordValues = risRecord.stream().toList();
                var recordKeyValue = risRecord.get(0).split("  -");
                if (recordKeyValue.length != 2 ) {
                    continue;
                }
                String key = StringEscapeUtils.escapeJava(risRecord.get(0).split("  -")[0].trim());
                if (END_FILE.equals(key)) {
                    references.add(reference);
                    reference = new HashMap<>();
                } else {
                    StringBuilder value = new StringBuilder(risRecord.get(0).split("  -")[1].trim());
                    if (recordValues.size() > 1) {
                        for (int i=1; i < recordValues.size(); i++) {
                            value.append(risRecord.get(i).trim());
                        }
                    }
                    reference.put(key, value.toString());
                }
            }

            return references;
        } catch (IOException e) {
            log.error("Error while parsing csv file", e);
            throw new FileLoadingException(multipartFile.getName());
        }
    }
}
