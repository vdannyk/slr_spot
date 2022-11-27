package com.dkwasniak.slr_spot_backend.criterion;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "criteria_types",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
@NoArgsConstructor
@Getter
@Setter
public class CriterionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
}
