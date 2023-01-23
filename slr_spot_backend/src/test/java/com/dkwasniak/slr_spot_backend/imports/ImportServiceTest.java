package com.dkwasniak.slr_spot_backend.imports;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ImportServiceTest {

    @InjectMocks
    private ImportService importService;

    @Mock
    private ImportRepository importRepository;

    @Test
    public void getPublicReviews_shouldReturnPublicReviews() {
        var import1 = new Import();
        List<Import> imports = new ArrayList<>() {{
            add(import1);
        }};

        when(importRepository.findByReview_Id(anyLong(), any())).thenReturn(new PageImpl<>(imports));
        var importPage = importService.getImportsByReviewId(1L, 0, 10);

        assertEquals(1, importPage.getContent().size());
    }
}
