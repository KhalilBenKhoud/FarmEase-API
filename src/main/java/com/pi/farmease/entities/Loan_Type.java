package com.pi.farmease.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(scope = Loan_Type.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "loanType_id")
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


    @OneToMany(cascade = CascadeType.ALL, mappedBy="loandId"  , fetch = FetchType.EAGER)

    private Set<Credit> credit;



}
