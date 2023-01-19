package com.dkwasniak.slr_spot_backend.screeningDecision;

import com.dkwasniak.slr_spot_backend.study.Stage;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "screening_decisions")
public class ScreeningDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @EmbeddedId
//    private ScreeningDecisionId id;
    @ManyToOne
    @JoinColumn(name = "study_id")
    @JsonIgnore
    private Study study;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
//    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    @MapsId("userId")
//    private User user;
//    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    @MapsId("studyId")
//    private Study study;

    @Enumerated(EnumType.STRING)
    private Decision decision;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    public ScreeningDecision(Decision decision, Stage stage) {
//        this.id = new ScreeningDecisionId(user.getId(), study.getId());
        this.decision = decision;
        this.stage = stage;
    }
}
