package com.dkwasniak.slr_spot_backend.imports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportRequest {

    private String searchValue;
    private String source;
    private MultipartFile file;
}
