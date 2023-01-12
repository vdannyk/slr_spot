package com.dkwasniak.slr_spot_backend.review;

import com.dkwasniak.slr_spot_backend.criterion.Criterion;
import com.dkwasniak.slr_spot_backend.folder.Folder;
import com.dkwasniak.slr_spot_backend.imports.Import;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWord;
import com.dkwasniak.slr_spot_backend.researchQuestion.ResearchQuestion;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.dkwasniak.slr_spot_backend.user.User;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
    private Integer numOfImportedStudies;
    private Integer numOfRemovedDuplicates;

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

    @OneToMany(mappedBy = "review", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<ResearchQuestion> researchQuestions = new HashSet<>();

    public Review(String title) {
        this.title = title;
    }

    public Review(String title, String researchArea, String description,
                  Boolean isPublic, Integer screeningReviewers) {
        this.title = title;
        this.researchArea = researchArea;
        this.description = description;
        this.isPublic = isPublic;
        this.screeningReviewers = screeningReviewers;
        this.numOfImportedStudies = 0;
        this.numOfRemovedDuplicates = 0;
    }

    public Review(String title, String researchArea, String description,
                  Boolean isPublic, Integer screeningReviewers, Integer numOfImportedStudies, Integer numOfRemovedDuplicates) {
        this.title = title;
        this.researchArea = researchArea;
        this.description = description;
        this.isPublic = isPublic;
        this.screeningReviewers = screeningReviewers;
        this.numOfImportedStudies = numOfImportedStudies;
        this.numOfRemovedDuplicates = numOfRemovedDuplicates;
    }

    public void addUser(User user, ReviewRole role) {
        UserReview userReview = new UserReview(user, this, role);
        this.users.add(userReview);
    }

    public void removeUser(User user) {
        for (Iterator<UserReview> it = users.iterator(); it.hasNext();) {
            UserReview userReview = it.next();
            if (userReview.getReview().equals(this) && userReview.getUser().equals(user)) {
                it.remove();
                this.users.remove(userReview);
            }
        }
    }

    public void addResearchQuestion(ResearchQuestion researchQuestion) {
        this.researchQuestions.add(researchQuestion);
    }
}
