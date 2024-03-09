package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "investments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Investment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private Date investedAt;

    @ManyToOne
    @JoinColumn(name = "investor_id")
    @JsonBackReference
    private User investor;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;



}

