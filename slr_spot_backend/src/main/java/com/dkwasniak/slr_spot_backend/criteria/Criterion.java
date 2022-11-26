package com.dkwasniak.slr_spot_backend.criteria;

import com.dkwasniak.slr_spot_backend.review.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "criteria")
@NoArgsConstructor
@Getter
@Setter
public class Criterion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String type; // exclusion, inclusion

    @ManyToMany(mappedBy = "criteria")
    private Set<Review> reviews = new HashSet<>();
}
