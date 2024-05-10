package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_application ;
    private String nom_application ;
    private String prenom_application ;
    private long interesting_rate_application;
    private String description_application;
    private String etat_application;
    private String message_application;

    @ManyToOne
    @JsonIgnore
    Mortgage mortgage;

    @ManyToOne
    @JsonBackReference
    private User user ;




}
