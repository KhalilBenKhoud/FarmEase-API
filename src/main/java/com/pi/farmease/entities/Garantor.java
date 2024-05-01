package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Builder

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Garantor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idGarantor ;
    private String nameGarantor ;
    private String secondnameGarantor ;
    private float salaryGarantor ;
    private String workGarantor ;
    private String qrString;

    @Lob
    private byte[] pdfDocument;

    public String getQrString() {
        return qrString;
    }

    public void setQrString(String qrString) {
        this.qrString = qrString;
    }

    @ManyToOne
    @JsonBackReference
    private User user ;

    @JsonIgnore
    @OneToOne
    private Credit credit;




}
