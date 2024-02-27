package com.pi.farmease.entities;

import com.pi.farmease.entities.enumerations.StatusInsurance;
import com.pi.farmease.entities.enumerations.StatusSinister;
import com.pi.farmease.entities.enumerations.TypeInsurance;
import com.pi.farmease.entities.enumerations.TypeSinister;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Sinister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    private Date date_Sinister ;
    private  String description ;
    private double  amount ;
    @Enumerated(EnumType.STRING)
    private TypeSinister type ;
    @Enumerated(EnumType.STRING)
    private StatusSinister status ;

    @ManyToOne
    private Insurance insurance;
}
