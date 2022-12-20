package com.dkwasniak.slr_spot_backend.document;

import com.dkwasniak.slr_spot_backend.study.Study;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

@Entity
@Table(name = "documents")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private Blob data;

    @OneToOne
    @JoinColumn(name = "study_id")
    @JsonIgnore
    private Study study;

    public void setData(byte[] data) {
        try {
            this.data = new SerialBlob(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public byte[] getData() {
        try {
            return data.getBytes(1, (int) data.length());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
