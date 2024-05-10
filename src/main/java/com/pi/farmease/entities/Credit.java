package com.pi.farmease.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Credit implements Serializable {

    @Id
    @Column(name ="idCredit")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCredit;

    private float amount;
    @Temporal (TemporalType.DATE)
    private Date dateDemande;


    @Temporal (TemporalType.DATE)
    private Date obtainingDate ;


    private Boolean differe;
    // PERIODE DE DIFFERE
    private float DIFF_period;


    @Temporal (TemporalType.DATE)
    private Date monthlyPaymentDate;
    private float monthlyPaymentAmount;
    //taux d'interet en année
    private float interestRate;
    //periode de credit en année
    private float creditPeriod;
    private float Risk;
    private Boolean Completed;
    private String Reason;


    @Enumerated(EnumType.STRING)
    private status_loan status ;

    @ManyToOne
    Loan_Type loandId;


    @OneToOne(mappedBy = "credit")
    private Garantor garantor;

    @ManyToOne
    @JsonBackReference
    private User user ;

    public Credit(float amount, float period, float interst) {
        this.amount=amount;
        this.creditPeriod=period;
        this.interestRate=interst;
    }


}


