package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.folder.Folder;
import com.dkwasniak.slr_spot_backend.imports.Import;
import com.dkwasniak.slr_spot_backend.keyWord.KeyWord;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
import com.dkwasniak.slr_spot_backend.study.status.Status;
import com.dkwasniak.slr_spot_backend.tag.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="studies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1000)
    private String title;
    @Column(length = 1000)
    private String authors;
    private String journalTitle;
    private Integer publicationYear;
    private String volume;
    private String doi;
    private String url;
    @Column(length = 5000)
    private String documentAbstract;
    private String issn;
    private String language;

    // TODO pdfs
    private String fullText;

    @ManyToOne
    @JoinColumn(name = "import_id")
    @JsonIgnore
    private Import studyImport;

    @ManyToOne
    @JoinColumn(name = "status_id")
    @JsonIgnore
    private Status status;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    @JsonIgnore
    private Folder folder;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "studies_tags",
            joinColumns = { @JoinColumn(name = "study_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Set<ScreeningDecision> screeningDecisions = new HashSet<>();

    @OneToMany(mappedBy = "study", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments = new ArrayList();

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getStudies().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getStudies().remove(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setStudy(this);
    }
}
