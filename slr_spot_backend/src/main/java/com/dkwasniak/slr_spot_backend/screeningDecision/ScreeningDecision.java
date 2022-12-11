package com.dkwasniak.slr_spot_backend.screeningDecision;

import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "screening_decisions")
public class ScreeningDecision {

    @EmbeddedId
    private ScreeningDecisionId id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId("userId")
    private User user;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId("studyId")
    private Study study;

    @Enumerated(EnumType.STRING)
    private Decision decision;

    public ScreeningDecision(User user, Study study, Stage stage, Decision decision) {
        this.id = new ScreeningDecisionId(user.getId(), study.getId(), stage);
        this.user = user;
        this.study = study;
        this.decision = decision;
    }
}
