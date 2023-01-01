package com.dkwasniak.slr_spot_backend.study.dto;

import com.dkwasniak.slr_spot_backend.study.Stage;
import com.dkwasniak.slr_spot_backend.study.StudyState;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor(staticName = "of")
@Getter
public class StatusDto {

    private Stage stage;
    private StudyState state;

}
