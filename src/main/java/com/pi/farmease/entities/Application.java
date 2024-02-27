package com.pi.farmease.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_application ;
    private String nom_application ;
    private String prenom_application ;
    private long interesting_rate_application;
    private String programme_description_application;

    @ToString.Exclude
    @ManyToOne
    Mortgage mortgage;




}
