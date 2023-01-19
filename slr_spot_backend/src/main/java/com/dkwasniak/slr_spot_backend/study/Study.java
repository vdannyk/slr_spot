package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.comment.Comment;
import com.dkwasniak.slr_spot_backend.study.document.Document;
import com.dkwasniak.slr_spot_backend.folder.Folder;
import com.dkwasniak.slr_spot_backend.imports.Import;
import com.dkwasniak.slr_spot_backend.operation.Operation;
import com.dkwasniak.slr_spot_backend.screeningDecision.ScreeningDecision;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    @Column(length = 250)
    private String title;
    @Column(length = 400)
    private String authors;
    @Column(length = 250)
    private String journalTitle;
    private Integer publicationYear;

    @Column(length = 40)
    private String volume;
    @Column(length = 250)
    private String doi;
    @Column(length = 250)
    private String url;
    @Column(length = 5000)
    private String documentAbstract;
    @Column(length = 40)
    private String issn;
    @Column(length = 40)
    private String language;

    @OneToOne(mappedBy = "study", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Document fullText;

    @ManyToOne
    @JoinColumn(name = "import_id")
    @JsonIgnore
    private Import studyImport;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    @Enumerated(EnumType.STRING)
    private StudyState state;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "studies_tags",
            joinColumns = { @JoinColumn(name = "study_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "study", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private List<ScreeningDecision> screeningDecisions = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments = new ArrayList();

    @OneToMany(mappedBy = "study", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private List<Operation> operations = new ArrayList();

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

    public void addOperation(Operation operation) {
        this.operations.add(operation);
        operation.setStudy(this);
    }

    public void addScreeningDecision(ScreeningDecision screeningDecision) {
        this.screeningDecisions.add(screeningDecision);
        screeningDecision.setStudy(this);
    }

    public void setDocument(Document document) {
        this.fullText = document;
        document.setStudy(this);
    }
}
