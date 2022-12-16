package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.criterion.Criterion;
import com.dkwasniak.slr_spot_backend.folder.Folder;
import com.dkwasniak.slr_spot_backend.imports.Import;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWord;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @OneToMany(mappedBy = "review", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "review", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<Criterion> criteria = new HashSet<>();

    @OneToMany(mappedBy = "review", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<KeyWord> keywords = new HashSet<>();

    @OneToMany(mappedBy = "review", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<Import> imports = new HashSet<>();

    @OneToMany(mappedBy = "review", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Folder> folders = new ArrayList<>();

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
