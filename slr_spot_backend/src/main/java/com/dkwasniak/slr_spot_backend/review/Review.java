package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
    private String owner;

    @ManyToMany(mappedBy = "reviews")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Review(String title) {
        this.title = title;
    }

    public Review(String title, String researchArea, String description, Boolean isPublic, Integer screeningReviewers, String owner) {
        this.title = title;
        this.researchArea = researchArea;
        this.description = description;
        this.isPublic = isPublic;
        this.screeningReviewers = screeningReviewers;
        this.owner = owner;
    }
}
