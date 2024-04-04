package com.pi.farmease.entities;

import com.pi.farmease.entities.enumerations.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    private String description ;
    private Date issuedAt ;

    @Enumerated(EnumType.STRING)
    private TransactionType type ;

}
