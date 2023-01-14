package com.dkwasniak.slr_spot_backend.tag;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Entity
@Table(name = "tags",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
@NoArgsConstructor
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 40)
    private String name;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @JsonIgnore
    private Review review;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<Study> studies;

    public Tag(String name) {
        this.name = name;
    }
}
