package com.pi.farmease.entities;

import com.pi.farmease.entities.enumerations.Role;
import com.pi.farmease.entities.enumerations.StatusInsurance;
import com.pi.farmease.entities.enumerations.TypeInsurance;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;


    private LocalDate start_date ;
    private  LocalDate end_date ;

    private  double coverage_amount;
    private  double premium;
    @Enumerated(EnumType.STRING)
    private TypeInsurance type ;
    @Enumerated(EnumType.STRING)
    private StatusInsurance status ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    private List<Sinister> sinisters ;

}
