package com.dkwasniak.slr_spot_backend.criteria;

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
@Table(name = "criteria",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "name", "type" })
        })
@NoArgsConstructor
@Getter
@Setter
public class Criterion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String type; // exclusion, inclusion

    public Criterion(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
