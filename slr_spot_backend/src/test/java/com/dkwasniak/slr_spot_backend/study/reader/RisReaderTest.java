package com.dkwasniak.slr_spot_backend.study.reader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RisReaderTest {

    private static final String RIS_STRING = "TI  - Test title\n" +
            "T2  - Test Journal\n" +
            "VL  - 27\n" +
            "IS  - 18\n" +
            "SP  - 2892\n" +
            "EP  - 2896\n" +
            "ER  - \n" +
            "\n" +
            "TI  - Test title 2\n" +
            "T2  - Journal\n" +
            "VL  - 127\n" +
            "SP  - 210\n" +
            "EP  - 221\n" +
            "PY  - 2023\n" +
            "DO  - 10.1016/j.jes\n" +
            "AU  - Test\n" +
            "ER  - ";

    @InjectMocks
    private RisReader risReader;

    @Test
    public void write_shouldWriteStudiesToResource_whenCorrect() throws IOException {
        var stream = new ByteArrayInputStream(RIS_STRING.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", stream);

        var res = risReader.read(mockMultipartFile);

        assertNotNull(res);
        assertEquals(2, res.size());
    }

}
