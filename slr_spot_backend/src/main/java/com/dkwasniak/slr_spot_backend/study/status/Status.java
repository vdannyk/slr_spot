package com.dkwasniak.slr_spot_backend.study.status;


import com.dkwasniak.slr_spot_backend.screeningDecision.Stage;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="statuses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Stage stage;

    @OneToMany(mappedBy = "status")
    @JsonIgnore
    private List<Study> studies = new ArrayList<>();
}
