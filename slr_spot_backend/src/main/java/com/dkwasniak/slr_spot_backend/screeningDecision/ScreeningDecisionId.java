package com.dkwasniak.slr_spot_backend.screeningDecision;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningDecisionId implements Serializable {

    private Long userId;
    private Long studyId;
    @Enumerated(EnumType.STRING)
    private Stage stage;
}
