package com.pi.farmease.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Loan_Type implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long loanType_id ;
    private String Name ;
    private float value ;
    private String Descprtion ;


    @Lob
    private byte[] image;
    @Enumerated(EnumType.STRING)
    private Type_Term_Loan termType ;


    @OneToMany(cascade = CascadeType.ALL, mappedBy="loandId")
    private Set<Credit> credit;



}
