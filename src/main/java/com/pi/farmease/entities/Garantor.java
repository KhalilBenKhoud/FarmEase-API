package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
@Builder

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(scope = Garantor.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "idGarantor")
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

    private User user ;


    @OneToOne

    private Credit credit;




}
