package com.dkwasniak.slr_spot_backend.imports;

import com.dkwasniak.slr_spot_backend.deduplication.DeduplicationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImportContext implements Serializable {

    private MultipartFile file;
    private Long reviewId;
    private String source;
    private String searchValue;
    private String additionalInfo;
    private Long userId;
    private DeduplicationType deduplicationType;
}
