package com.pi.farmease.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "performances")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Performance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double netIncome;

    private Integer year;

    // Additional fields for performance calculations
    private Double totalInvestment;
    private Double currentMarketValue;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
