package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private long id_comment ;
    private String description_comment;
    private LocalDate date_comment;
    private long nbr_siginal_comment;
    private long nbr_like_comment;


    @ManyToOne
    @JsonIgnore
    Post post;


    @ManyToOne
    @JsonBackReference
    private User user ;
}
