package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.imports.Import;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    public Study(String title, String authors, String journalTitle, Integer publicationYear, String volume, String doi,
                 String url, String documentAbstract, String issn, String language, String fullText) {
        this.title = title;
        this.authors = authors;
        this.journalTitle = journalTitle;
        this.publicationYear = publicationYear;
        this.volume = volume;
        this.doi = doi;
        this.url = url;
        this.documentAbstract = documentAbstract;
        this.issn = issn;
        this.language = language;
        this.fullText = fullText;
    }
}
