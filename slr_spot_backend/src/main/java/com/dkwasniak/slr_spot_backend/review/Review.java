package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.criterion.Criterion;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWord;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reviews")
@NoArgsConstructor
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String researchArea;
    private String description;
    private Boolean isPublic;
    private Integer screeningReviewers;

    @OneToMany(mappedBy = "review", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Set<UserReview> users = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "reviews_tags",
            joinColumns = { @JoinColumn(name = "review_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "reviews_criteria",
            joinColumns = { @JoinColumn(name = "review_id") },
            inverseJoinColumns = { @JoinColumn(name = "criterion_id") }
    )
    private Set<Criterion> criteria = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "reviews_keywords",
            joinColumns = { @JoinColumn(name = "review_id") },
            inverseJoinColumns = { @JoinColumn(name = "keyword_id") }
    )
    private Set<KeyWord> keywords = new HashSet<>();

    public Review(String title) {
        this.title = title;
    }

    public Review(String title, String researchArea, String description, Boolean isPublic, Integer screeningReviewers) {
        this.title = title;
        this.researchArea = researchArea;
        this.description = description;
        this.isPublic = isPublic;
        this.screeningReviewers = screeningReviewers;
    }
}
