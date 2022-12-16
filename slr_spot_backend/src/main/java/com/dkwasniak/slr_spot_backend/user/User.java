package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.keyWord.KeyWord;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
import com.dkwasniak.slr_spot_backend.screeningDecision.Stage;
import com.dkwasniak.slr_spot_backend.study.Study;
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
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name="users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean isActivated = false;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Set<UserReview> reviews = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<KeyWord> keywords = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Set<ScreeningDecision> screeningDecisions = new HashSet<>();

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void addDecision(Study study, Stage stage, Decision decision) {
        ScreeningDecision screeningDecision = new ScreeningDecision(this, study, stage, decision);
        this.screeningDecisions.add(screeningDecision);
    }

    public void removeDecision(Study study, Stage stage, Decision decision) {
        // TODO add if needed undo decisions
    }
}
