package com.dkwasniak.slr_spot_backend.criterion;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private CriterionType type;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @JsonIgnore
    private Review review;

    public Criterion(String name, CriterionType type) {
        this.name = name;
        this.type = type;
    }
}
