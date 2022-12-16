package com.dkwasniak.slr_spot_backend.imports.dto;

import com.dkwasniak.slr_spot_backend.imports.Import;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportDto {

    private Import studyImport;
    private Integer studiesAdded;
    private Integer duplicatesRemoved;

}
