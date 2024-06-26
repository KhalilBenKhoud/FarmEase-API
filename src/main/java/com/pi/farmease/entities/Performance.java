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

    private String projectTitle;

    private Double netIncome;


    private double annualProjectedProfit;

    private Integer year;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "project_id")
    private Project project;
}
