package com.dkwasniak.slr_spot_backend.keyWord;

import com.dkwasniak.slr_spot_backend.review.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "keywords",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "name", "type" })
        })
@NoArgsConstructor
@Getter
@Setter
public class KeyWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String type; // positive, negative

}
