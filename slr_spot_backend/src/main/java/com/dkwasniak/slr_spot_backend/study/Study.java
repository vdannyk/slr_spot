package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.imports.Import;
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
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
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
    private String documentTitle;
    @Column(length = 1000)
    private String authors;
    private String publicationTitle;
    private Integer publicationYear;
    private String volume;
    @Column(length = 5000)
    private String documentAbstract;
    private String issn;
    private String url;
    private String fullText;

    @ManyToOne
    @JoinColumn(name = "import_id", nullable = false)
    private Import studyImport;

}
