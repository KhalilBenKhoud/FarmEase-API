package com.pi.farmease.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Post ;
    private String title_Post ;
    private String description_Post ;
    private Date date_Post  ;
    private long nbr_like_post ;
    private long nbr_signal_post ;
    private String category_post ;
    private String sondage1 ;
    private String sondage2 ;
    private double stat1 ;
    private double stat2 ;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "post")
    private Set<Comment> Commentaire;

    @ManyToOne
    @JsonBackReference
    private User user ;





}
