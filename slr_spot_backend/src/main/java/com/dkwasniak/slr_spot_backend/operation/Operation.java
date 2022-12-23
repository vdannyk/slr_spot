package com.dkwasniak.slr_spot_backend.operation;

import com.dkwasniak.slr_spot_backend.study.Study;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.Instant.now;

@Entity
@Table(name="operations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private String description;

    @ManyToOne
    @JoinColumn(name = "study_id")
    @JsonIgnore
    private Study study;

    public Operation(String description) {
        this.date = LocalDateTime.ofInstant(now(), ZoneId.systemDefault());
        this.description = description;
    }
}
