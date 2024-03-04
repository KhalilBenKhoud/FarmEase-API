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
    private Long id_forum ;
    private String title_forum ;
    private String description_forum ;
    private Date date_forum  ;
    private long nbr_like ;
    private long nbr_signal ;
    private String category_forum ;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "forum")
    private Set<Comment> Commentaire;

    @ManyToOne
    @JsonBackReference
    private User user ;





}
