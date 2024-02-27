package com.pi.farmease.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_commentaire ;
    private String description_commentaire;
    private LocalDate date_commentaire;
    private long nbr_siginal_commentaire;
    private long nbr_like_commentaire;

    @ToString.Exclude
    @ManyToOne
    Post forum;
}
