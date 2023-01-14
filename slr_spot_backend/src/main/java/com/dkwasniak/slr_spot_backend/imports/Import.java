package com.dkwasniak.slr_spot_backend.imports;


import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.study.Study;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

@Entity
@Table(name="imports")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Import {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    @Column(length = 100)
    private String searchValue;
    @Column(length = 40)
    private String source;
    @Column(length = 1000)
    private String additionalInformation;
    @Column(length = 100)
    private String performedBy;
    private Integer numOfImportedStudies;
    private Integer numOfRemovedDuplicates;

    @OneToMany(mappedBy = "studyImport", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private List<Study> studies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @JsonIgnore
    private Review review;

    public Import(String searchValue, String source, String additionalInformation,
                  String performedBy, Integer numOfImportedStudies, Integer numOfRemovedDuplicates) {
        this.date = LocalDateTime.ofInstant(now(), ZoneId.systemDefault());
        this.searchValue = searchValue;
        this.source = source;
        this.additionalInformation = additionalInformation;
        this.performedBy = performedBy;
        this.numOfImportedStudies = numOfImportedStudies;
        this.numOfRemovedDuplicates = numOfRemovedDuplicates;
    }
}
