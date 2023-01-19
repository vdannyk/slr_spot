package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWord;
import com.dkwasniak.slr_spot_backend.screeningDecision.Decision;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
import com.dkwasniak.slr_spot_backend.study.Stage;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Column(length = 40)
    private String firstName;
    @Column(length = 40)
    private String lastName;
    private String email;
    private String password;
    private Boolean isActivated = false;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Set<UserReview> reviews = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<KeyWord> keywords = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Set<ScreeningDecision> screeningDecisions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments = new ArrayList();

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void addScreeningDecision(ScreeningDecision screeningDecision) {
        this.screeningDecisions.add(screeningDecision);
        screeningDecision.setUser(this);
    }

}
