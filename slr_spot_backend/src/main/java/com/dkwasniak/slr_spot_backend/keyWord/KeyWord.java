package com.dkwasniak.slr_spot_backend.keyWord;

import com.dkwasniak.slr_spot_backend.criterion.CriterionType;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "keywords",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "name", "criterion_type_id" })
        })
@NoArgsConstructor
@Getter
@Setter
public class KeyWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "criterion_type_id")
    private CriterionType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @JsonIgnore
    private Review review;

    public KeyWord(String name, CriterionType type) {
        this.name = name;
        this.type = type;
    }

    public KeyWord(String name, CriterionType type, Review review) {
        this.name = name;
        this.type = type;
        this.review = review;
    }

}
