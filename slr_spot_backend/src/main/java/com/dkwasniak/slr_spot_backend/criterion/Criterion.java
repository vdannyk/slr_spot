package com.dkwasniak.slr_spot_backend.criterion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "criteria",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "name", "criterion_type_id" })
        })
@NoArgsConstructor
@Getter
@Setter
public class Criterion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "criterion_type_id")
    private CriterionType type;

    public Criterion(String name, CriterionType type) {
        this.name = name;
        this.type = type;
    }
}
